package com.ummshsh.rssreader.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ummshsh.rssreader.RssFetcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

//    private val rssList = MutableLiveData<RssFeed?>()

    private val _allAtOnceString = MutableLiveData<String>()
    val allAtOnceString: LiveData<String>
        get() = _allAtOnceString

    init {
        getRssFeeds()

    }

    private fun getRssFeeds() {
        viewModelScope.launch {
            val fetcher = RssFetcher()
            val list = fetcher.fetchEntries("https://www.rationalanswer.ru/feed/")
            _allAtOnceString.value = list.joinToString()
        }
    }
}
