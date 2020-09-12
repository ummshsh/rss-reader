package com.ummshsh.rssreader.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ummshsh.rssreader.database.ArticleDatabase
import com.ummshsh.rssreader.database.DbHelper
import com.ummshsh.rssreader.network.RssFetcher
import com.ummshsh.rssreader.network.asDatabaseArticles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticlesRepository(private val database: DbHelper) {

    private val _articles = MutableLiveData<List<ArticleDatabase>>()
    val articles: LiveData<List<ArticleDatabase>>
        get() = _articles


    /**
     * This one should be called differently
     *
     * and should:
     * 1. Get feeds from database object
     * 2. Get articles for each feed from database object by
     *  Getting articles using RssFetcher
     *  Store them in database
     * 3. Get all articles from database and update _articles LiveData
     */
    suspend fun refreshArticles() {
        withContext(Dispatchers.IO) {
            val feeds = database.getFeeds()

            if (!feeds.any()) return@withContext

            for (feed in feeds) { //TODO: check this !! later
                val articles = RssFetcher().fetchEntries(feed.link).asDatabaseArticles(feed.id)
                database.insert(articles)
                _articles.postValue(database.getArticles())
            }
        }
    }
}