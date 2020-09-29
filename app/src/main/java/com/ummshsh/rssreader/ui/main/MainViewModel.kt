package com.ummshsh.rssreader.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.ummshsh.rssreader.database.ArticleDatabase
import com.ummshsh.rssreader.database.DbHelper
import com.ummshsh.rssreader.model.ArticleStatus
import com.ummshsh.rssreader.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var displayUnread = true
    private var displayAscending = true
    private val database = DbHelper(application)
    private val repository = Repository(database)

    private var _articles = MutableLiveData<List<ArticleDatabase>>()
    val articles: LiveData<List<ArticleDatabase>>
        get() = _articles

    init {
        toggleOnlyUnreadArticles()
        repository.refreshALl()
        _articles = repository.articles
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun refreshData() {
        repository.refreshALl()
    }

    fun markArticlesAsRead(isRead: Boolean): List<Int> {
        var articles = articles.value!!.map { it.id }
        repository.markArticlesRead(isRead, *articles.toIntArray())
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
