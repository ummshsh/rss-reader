package com.ummshsh.rssreader.model

data class ArticleLight(
    val id: Int = 0,
    val guid: String,
    val feedId: Int = 0,
    val title: String,
    val contentsShort: String,
    val description: String,
    val url: String,
    val isRead: Boolean = false
)