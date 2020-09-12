package com.ummshsh.rssreader.database

data class ArticleDatabase(
    val id: Long = 0L,
    val guid: String,
    val feedId: Long? = 0L,
    val title: String,
    val contents: String,
    val description: String,
    val url: String
)

data class Feed(
    val id: Long? = 0L,
    val title: String,
    val link: String,
    val folderId: Long
)

data class Folder(
    val id: Long? = 0L,
    val name: String
)