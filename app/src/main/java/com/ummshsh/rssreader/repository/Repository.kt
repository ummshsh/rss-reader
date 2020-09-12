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

    suspend fun refreshArticles() {
        // TODO: 13.09.2020 is this a good way to solve network lag before showing something from database?
        withContext(Dispatchers.IO){
            _articles.postValue(database.getArticles())
        }

        withContext(Dispatchers.IO) {
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
        return  articles.filter { !foundGuids.contains(it.guid) }
    }
}