package com.ummshsh.rssreader.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ummshsh.rssreader.database.ArticleDatabase
import com.ummshsh.rssreader.database.DbHelper
import com.ummshsh.rssreader.database.Feed
import com.ummshsh.rssreader.network.RssFetcher
import com.ummshsh.rssreader.network.asDatabaseArticles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticlesRepository(private val database: DbHelper) {

    private val _articles = MutableLiveData<List<ArticleDatabase>>()
    val articles: LiveData<List<ArticleDatabase>>
        get() = _articles

    private val _feeds = MutableLiveData<List<Feed>>()
    val feeds: LiveData<List<Feed>>
        get() = _feeds

    suspend fun refreshFeeds() {
        withContext(Dispatchers.IO) {
            _feeds.postValue(database.getFeeds())
        }
    }

    suspend fun refreshArticles() {
        withContext(Dispatchers.IO) {
            _articles.postValue(database.getArticles())
            val feeds = database.getFeeds()
            if (feeds.any()) {
                for (feed in feeds) {
                    val articles = RssFetcher().fetchEntries(feed.link).asDatabaseArticles(feed.id)
                    database.insert(filterExistingArticlesBeforeInsert(articles))
                    _articles.postValue(database.getArticles())
                }
            }
        }
    }

    private fun filterExistingArticlesBeforeInsert(articles: List<ArticleDatabase>): List<ArticleDatabase> {
        val foundGuids: List<String> = database.getArticlesWithGuids(articles.map { it.guid })
        return articles.filter { !foundGuids.contains(it.guid) }
    }

    fun addFeed(title: String, url: String) {
        database.insert(Feed(-1, title, url, -1))
    }

    fun deleteFeed(id: Int) {
        database.deleteFeed(id)
    }
}