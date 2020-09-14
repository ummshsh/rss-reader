package com.ummshsh.rssreader.ui.main

import android.app.Application
import androidx.lifecycle.*
import com.ummshsh.rssreader.database.*
import com.ummshsh.rssreader.repository.ArticlesRepository
import kotlinx.coroutines.*

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val database = DbHelper(application)
    private val repository = ArticlesRepository(database)

    private val _articles = MutableLiveData<List<ArticleDatabase>>()
    val articles: LiveData<List<ArticleDatabase>>
        get() = _articles

    init {
        getArticles()
    }

    private fun getArticles() {
        viewModelScope.launch {
            repository.refreshArticles()
            _articles.value = repository.articles.value
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun refreshArticles() {
        viewModelScope.launch {
            repository.refreshArticles()
        }
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
