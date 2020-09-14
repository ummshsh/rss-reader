package com.ummshsh.rssreader.ui.feedmanagement

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ummshsh.rssreader.database.DbHelper
import com.ummshsh.rssreader.database.Feed
import com.ummshsh.rssreader.repository.ArticlesRepository
import kotlinx.coroutines.*

class FeedManagementViewModel(var application: Application) : ViewModel() {

    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val database = DbHelper(application)
    private val repository = ArticlesRepository(database)

    private val _feeds = MutableLiveData<List<Feed>>()
    val feeds: LiveData<List<Feed>>
        get() = _feeds

    init {
        getFeeds()
    }

    private fun getFeeds() {
        viewModelScope.launch {
            // TODO: to move this to dispatchers.IO maybe
            repository.refreshFeeds()
            _feeds.value = repository.feeds.value
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    suspend fun addFeed(url: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.addFeed(url, url)
                getFeeds()
                repository.refreshArticles()
            }
        }
    }

    fun deleteFeed(id: Int) {
        viewModelScope.launch {
            repository.deleteFeed(id)
            repository.refreshFeeds()
            repository.refreshArticles()

            Toast.makeText(application, "DELETE $id", Toast.LENGTH_SHORT).show()
        }
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FeedManagementViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FeedManagementViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}