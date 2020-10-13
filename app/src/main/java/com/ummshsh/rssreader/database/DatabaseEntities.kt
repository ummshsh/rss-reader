package com.ummshsh.rssreader.database

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

data class FeedDatabase(
    val id: Int = 0,
    val title: String,
    val link: String,
    val folderId: Long
)

data class FolderDatabase(
    val id: Int = 0,
    val name: String
)