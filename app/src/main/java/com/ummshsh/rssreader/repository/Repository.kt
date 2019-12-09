package com.ummshsh.rssreader.repository

import androidx.lifecycle.Observer
import com.ummshsh.rssreader.database.AppDatabase
import com.ummshsh.rssreader.network.RssFetcher
import com.ummshsh.rssreader.network.asDatabaseArticles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticlesRepository(private val database: AppDatabase) {

    val articles = database.articleDao.getArticles()
    val feeds = database.feedDao.getFeeds()
    val folders = database.folderDao.getFolders()

    suspend fun refreshArticles() {
        withContext(Dispatchers.IO) {
            val feeds = database.feedDao.getFeeds()

            if (feeds.value == null) return@withContext

            for (feed in feeds.value!!) { //TODO: check this !! later
                val articles = RssFetcher().fetchEntries(feed.link).asDatabaseArticles(feed.id)
                database.articleDao.insert(articles)
            }
        }
    }
}