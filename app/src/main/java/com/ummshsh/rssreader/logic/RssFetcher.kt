package com.ummshsh.rssreader.logic

import com.prof.rssparser.Article
import com.prof.rssparser.Parser

class RssFetcher {
    suspend fun fetchEntries(url: String): MutableList<Article> {
        val parser = Parser()
        return parser.getArticles(url)
    }
}