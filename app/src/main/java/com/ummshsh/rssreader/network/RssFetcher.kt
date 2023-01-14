package com.ummshsh.rssreader.network

import com.prof.rssparser.Article
import com.prof.rssparser.OnTaskCompleted
import com.prof.rssparser.Parser
import com.ummshsh.rssreader.database.ArticleDatabase
import com.ummshsh.rssreader.database.Feed
import com.ummshsh.rssreader.database.IFeed
import com.ummshsh.rssreader.database.InvalidFeed

class RssFetcher {
    private val parser: Parser = Parser()

    suspend fun fetchEntries(url: String): List<Article> {
        return parser.getArticles(url)
    }

    fun getFeed(url: String): IFeed {
        // Replace this piece of code with my own rss crawler
        var feedToRetrun: IFeed = InvalidFeed
        parser.onFinish(object : OnTaskCompleted {
            override fun onError(e: Exception) {
                feedToRetrun = InvalidFeed
            }

            override fun onTaskCompleted(list: MutableList<Article>) {
                feedToRetrun = Feed(-1, "TITLE", "LINK", -1)
            }

        })
        parser.execute(url)
        return feedToRetrun
    }
}

fun List<Article>.asDatabaseArticles(feedId: Int): List<ArticleDatabase> {
    return map {
        ArticleDatabase(
            -1,
            it.guid.toString(),
            feedId,
            it.title.toString(),
            (if (it.content == null) it.description.toString() else it.content.toString()),
            it.description.toString(),
            it.link.toString()
        )
    }
}