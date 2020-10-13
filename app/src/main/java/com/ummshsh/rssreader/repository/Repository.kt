package com.ummshsh.rssreader.repository

import androidx.lifecycle.MutableLiveData
import com.ummshsh.rssreader.database.ArticleDatabase
import com.ummshsh.rssreader.database.DbHelper
import com.ummshsh.rssreader.database.FeedDatabase
import com.ummshsh.rssreader.model.ArticleLight
import com.ummshsh.rssreader.model.ArticleStatus
import com.ummshsh.rssreader.network.RssFetcher
import com.ummshsh.rssreader.network.asDatabaseArticles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Repository(private val database: DbHelper) {

    private var onlyArticlesStatus: ArticleStatus = ArticleStatus.All
    private var ascending: Boolean = false
    private var feedId: Int = -1

    private val _articles = MutableLiveData<List<ArticleLight>>()
    val articles: MutableLiveData<List<ArticleLight>>
        get() {
            refreshArticles()
            return _articles
        }

    private val _feeds = MutableLiveData<List<FeedDatabase>>()
    val feeds: MutableLiveData<List<FeedDatabase>>
        get() {
            refreshFeeds()
            return _feeds
        }

    private fun refreshFeeds() = _feeds.postValue(database.getFeeds())

    private fun refreshArticles() {
        _articles.postValue(
            database.getArticles(onlyArticlesStatus, ascending, feedId).asLightArticles()
        )

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
                _articles.postValue(
                    database.getArticles(onlyArticlesStatus, ascending, feedId).asLightArticles()
                )
            }
        }
    }

    private fun filterExistingArticlesBeforeInsert(articles: List<ArticleDatabase>): List<ArticleDatabase> {
        val foundGuidList = database.getArticlesWithGuids(articles.map { it.guid })
        return articles.filter { !foundGuidList.contains(it.guid) }
    }

    fun addFeed(title: String, url: String) {
        database.insert(FeedDatabase(-1, title, url, -1))
        refreshALl()
    }

    fun markArticlesRead(isRead: Boolean, vararg articleIds: Int) {
        database.markArticleAsRead(isRead, *articleIds)
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

    fun exposeOnly(articleStatus: ArticleStatus) {
        onlyArticlesStatus = articleStatus
        refreshArticles()
    }

    fun exposeAscending(ascending: Boolean) {
        this.ascending = ascending
        refreshArticles()
    }

    fun showOnlyFeed(feedId: Int) {
        this.feedId = feedId
        this.refreshArticles()
    }
}

private fun List<ArticleDatabase>.asLightArticles(): List<ArticleLight> {
    return map {
        ArticleLight(
            it.id,
            it.guid,
            it.feedId,
            it.title,
            if (it.contents.length > 30) it.contents.subSequence(0, 30).toString() else it.contents,
            it.description,
            it.url,
            it.isRead
        )

    }
}
