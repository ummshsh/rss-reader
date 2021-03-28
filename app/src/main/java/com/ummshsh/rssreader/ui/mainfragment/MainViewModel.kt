package com.ummshsh.rssreader.ui.mainfragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.ummshsh.rssreader.database.DbHelper
import com.ummshsh.rssreader.model.ArticleLight
import com.ummshsh.rssreader.model.ArticleStatus
import com.ummshsh.rssreader.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

    private var displayUnread = true
    private var displayAscending = true
    private var feedId: Int = -1

    private val database = DbHelper(application)
    private val repository = Repository(database)

    private var _articles = MutableLiveData<List<ArticleLight>>()
    val articles: LiveData<List<ArticleLight>>
        get() = _articles

    init {
        Log.i("MainViewModel","Created + ${this.toString()}")
        toggleOnlyUnreadArticles()
        toggleSorting()
        repository.refreshAll()
        _articles = repository.articles
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun markArticlesAsRead(isRead: Boolean): List<Int> {
        var articles = articles.value!!.map { it.id }
        viewModelScope.launch {
            repository.markArticlesRead(isRead, *articles.toIntArray())
        }
        return articles
    }

    fun toggleOnlyUnreadArticles() {
        displayUnread = !displayUnread
        repository.exposeOnly(if (displayUnread) ArticleStatus.Unread else ArticleStatus.All)
    }

    fun toggleSorting() {
        displayAscending = !displayAscending
        repository.exposeAscending(displayAscending)
    }

    fun showOnlyFeed(feedId: Int) {
        this.feedId = feedId
        repository.showOnlyFeed(this.feedId)
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
