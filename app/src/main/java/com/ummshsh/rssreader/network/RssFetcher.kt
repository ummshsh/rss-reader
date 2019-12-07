package com.ummshsh.rssreader.network

import com.prof.rssparser.Article
import com.prof.rssparser.Parser

class RssFetcher {
    suspend fun fetchEntries(url: String): MutableList<Article> {
        val parser = Parser()
        return parser.getArticles(url)
    }
}