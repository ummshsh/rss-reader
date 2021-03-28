package com.ummshsh.rssreader.model

data class ArticleDatabase(
    val id: Int = 0,
    val guid: String,
    val feedId: Int = 0,
    val title: String,
    val contents: String,
    val description: String,
    val url: String,
    val isRead: Boolean = false
)