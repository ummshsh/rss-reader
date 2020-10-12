package com.ummshsh.rssreader

import android.app.Application
import androidx.lifecycle.*
import com.ummshsh.rssreader.database.ArticleDatabase
import com.ummshsh.rssreader.database.DbHelper
import com.ummshsh.rssreader.database.Feed
import com.ummshsh.rssreader.repository.Repository
import com.ummshsh.rssreader.ui.main.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.IO + viewModelJob)

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

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}