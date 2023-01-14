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

interface IFeed {
    val id: Int
    val title: String
    val link: String
    val folderId: Long
}

data class Feed(
    override val id: Int = 0,
    override val title: String,
    override val link: String,
    override val folderId: Long
) : IFeed

object InvalidFeed : IFeed{
    override val id: Int = -1
    override val title: String = ""
    override val link: String = ""
    override val folderId: Long = -1
}

data class Folder(
    val id: Int = 0,
    val name: String
)