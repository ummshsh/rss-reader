package com.ummshsh.rssreader.network

import com.prof.rssparser.Article
import com.prof.rssparser.Parser
import com.ummshsh.rssreader.database.ArticleDatabase

class RssFetcher {
    suspend fun fetchEntries(url: String): List<Article> {
        val parser = Parser()
        return parser.getArticles(url)
    }
}

fun List<Article>.asDatabaseArticles(feedId: Int): List<ArticleDatabase> {
    return map {
        ArticleDatabase(
            -1,
            it.guid.toString(),
            feedId,
            it.title.toString(),
            it.content.toString(),
            it.description.toString(),
            it.link.toString()
        )
    }
}