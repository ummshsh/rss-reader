package com.ummshsh.rssreader.ui.articleviewfragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ummshsh.rssreader.database.DbHelper
import com.ummshsh.rssreader.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class ArticleViewModel(articleId: Int, application: Application) : ViewModel() {

    private var viewModelJob = Job()
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val database = DbHelper(application)
    private val repository = Repository(database)

    var title: String
    var feed: String
    var content: String


    init {
        var article = repository.getArticle(articleId)

        title = article.title
        feed = repository.getFeedName(article.feedId)
        content = article.contents
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(private val id: Int, private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ArticleViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ArticleViewModel(id, app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}