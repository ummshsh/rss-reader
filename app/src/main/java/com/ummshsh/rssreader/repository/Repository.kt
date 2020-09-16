package com.ummshsh.rssreader.repository

import androidx.lifecycle.MutableLiveData
import com.ummshsh.rssreader.database.ArticleDatabase
import com.ummshsh.rssreader.database.DbHelper
import com.ummshsh.rssreader.database.Feed
import com.ummshsh.rssreader.network.RssFetcher
import com.ummshsh.rssreader.network.asDatabaseArticles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Repository(private val database: DbHelper) {

    private val _articles = MutableLiveData<List<ArticleDatabase>>()
    val articles: MutableLiveData<List<ArticleDatabase>>
        get() {
            refreshArticles()
            return _articles
        }

    private val _feeds = MutableLiveData<List<Feed>>()
    val feeds: MutableLiveData<List<Feed>>
        get() {
            refreshFeeds()
            return _feeds
        }

    private fun refreshFeeds() = _feeds.postValue(database.getFeeds())

    private fun refreshArticles() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val feeds = database.getFeeds()
                if (feeds.any()) {
                    for (feed in feeds) {
                        val articles =
                            RssFetcher().fetchEntries(feed.link).asDatabaseArticles(feed.id)
                        database.insert(filterExistingArticlesBeforeInsert(articles))
                    }
                }
                _articles.postValue(database.getArticles())
            }
        }
    }

    private fun filterExistingArticlesBeforeInsert(articles: List<ArticleDatabase>): List<ArticleDatabase> {
        val foundGuidList = database.getArticlesWithGuids(articles.map { it.guid })
        return articles.filter { !foundGuidList.contains(it.guid) }
    }

    fun addFeed(title: String, url: String) {
        database.insert(Feed(-1, title, url, -1))
        refreshALl()
    }

    fun deleteFeed(id: Int) {
        database.deleteFeed(id)
        refreshALl()
    }

    fun refreshALl() {
        refreshFeeds()
        refreshArticles()
    }

    fun getArticle(articleId: Int): ArticleDatabase = database.getArticle(articleId)

    fun getFeedName(feedId: Int): String = database.getFeedName(feedId)
}