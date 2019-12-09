package com.ummshsh.rssreader.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.ummshsh.rssreader.database.AppDatabase
import com.ummshsh.rssreader.database.ArticleDatabase
import com.ummshsh.rssreader.database.Feed
import com.ummshsh.rssreader.database.Folder
import com.ummshsh.rssreader.repository.ArticlesRepository
import kotlinx.coroutines.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val database = AppDatabase.getInstance(application)
    private val repository = ArticlesRepository(database)

    private val _articles = MutableLiveData<List<ArticleDatabase>>()
    val articles: LiveData<List<ArticleDatabase>>
        get() = _articles

    init {
        getRssFeeds()
    }

    //TODO: to delete later
    suspend fun addFeedTesting() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val folderId = database.folderDao.insert(Folder(null, "Default folder"))
                var feed = Feed(
                    null,
                    "Title",
                    "https://waitbutwhy.com/feed",
                    folderId
                )
                database.feedDao.insert(feed)
            }
        }
    }

    private fun getRssFeeds() {
        viewModelScope.launch {
            // TODO: to move this to dispatchers.IO maybe
            repository.refreshArticles()
            _articles.value = repository.articles.value
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}
