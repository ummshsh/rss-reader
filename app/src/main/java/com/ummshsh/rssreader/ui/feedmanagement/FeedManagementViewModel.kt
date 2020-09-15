package com.ummshsh.rssreader.ui.feedmanagement

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ummshsh.rssreader.database.DbHelper
import com.ummshsh.rssreader.database.Feed
import kotlinx.coroutines.*
import  com.ummshsh.rssreader.repository.Repository

class FeedManagementViewModel(var application: Application) : ViewModel() {

    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val database = DbHelper(application)
    private val repository = Repository(database)

    private var _feeds = MutableLiveData<List<Feed>>()
    val feeds: LiveData<List<Feed>>
        get() = _feeds

    init {
        repository.refreshALl()
        _feeds = repository.feeds
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun addFeed(url: String) {
        viewModelScope.launch {
            repository.addFeed(url, url)
        }
    }

    fun deleteFeed(id: Int) {
        viewModelScope.launch {
            repository.deleteFeed(id)
            Toast.makeText(application, "DELETE $id", Toast.LENGTH_SHORT).show()
        }
    }

    fun refreshData() {
        repository.refreshALl()
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