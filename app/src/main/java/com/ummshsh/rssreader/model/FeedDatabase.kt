package com.ummshsh.rssreader.model

data class FeedDatabase(
    val id: Int,
    val title: String,
    val link: String,
    val folderId: Long = 0
) {
    companion object Feed {
        val EmptyFeed: FeedDatabase = FeedDatabase(-1, "", "", 0)
    }

    val isEmpty: Boolean = id < 0 && title == ""
}
