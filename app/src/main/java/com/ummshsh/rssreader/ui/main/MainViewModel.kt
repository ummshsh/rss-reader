package com.ummshsh.rssreader.ui.main

import android.app.Application
import android.content.ContentValues
import androidx.lifecycle.*
import com.ummshsh.rssreader.database.*
import com.ummshsh.rssreader.repository.ArticlesRepository
import kotlinx.coroutines.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val database = DbHelper(application)
    private val repository = ArticlesRepository(database)

    // TODO: 9/12/2020 I have same exact copy of live data in Repository
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
                val values = ContentValues().apply {
                    put(DatabaseContract.Feed.COLUMN_NAME_TITLE, "WBY")
                    put(DatabaseContract.Feed.COLUMN_NAME_LINK, "https://waitbutwhy.com/feed")
                }
                database
                    .writableDatabase
                    .insert(DatabaseContract.Feed.TABLE_NAME, null, values)
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
