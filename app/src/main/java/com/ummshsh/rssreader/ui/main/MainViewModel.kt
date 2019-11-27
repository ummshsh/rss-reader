package com.ummshsh.rssreader.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val rssList = MutableLiveData<RssFeed?>()

    init {
        getRssFeeds()

    }

    private fun getRssFeeds() {
        viewModelScope.launch {
            // TODO: fetch results using RssFetcher
        }
    }
}
