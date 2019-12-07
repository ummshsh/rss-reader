package com.ummshsh.rssreader.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prof.rssparser.Article
import com.ummshsh.rssreader.network.RssFetcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>>
        get() = _articles

    init {
        getRssFeeds()
    }

    private fun getRssFeeds() {
        viewModelScope.launch {// TODO: to move this to dispatchers.IO maybe
            val fetcher = RssFetcher()
            val list = fetcher.fetchEntries("https://www.rationalanswer.ru/feed/")
            _articles.value = list
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
