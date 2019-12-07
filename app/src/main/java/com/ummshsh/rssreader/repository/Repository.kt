package com.ummshsh.rssreader.repository

import androidx.lifecycle.LiveData
import com.ummshsh.rssreader.database.AppDatabase
import com.ummshsh.rssreader.database.ArticleDatabase
import com.ummshsh.rssreader.database.Feed
import com.ummshsh.rssreader.database.Folder
import com.ummshsh.rssreader.network.RssFetcher
import com.ummshsh.rssreader.network.asDatabaseArticles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ArticlesRepository(private val database: AppDatabase) {

    val articles: LiveData<List<ArticleDatabase>> = database.articleDao.getArticles()
    val feeds: LiveData<List<Feed>> = database.feedDao.getFeeds()
    val folders: LiveData<List<Folder>> = database.folderDao.getFolders()

    suspend fun reefreshArticles() {
        withContext(Dispatchers.IO) {

            val feeds = database.feedDao.getFeeds()
            for (feed in feeds.value!!) { //TODO: check this !! later
                val articles = RssFetcher().fetchEntries(feed.link).asDatabaseArticles(feed.id)
                database.articleDao.insert(articles)
            }
        }
    }
}