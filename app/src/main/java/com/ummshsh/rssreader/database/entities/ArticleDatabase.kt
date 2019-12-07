package com.ummshsh.rssreader.database.entities

import androidx.room.*

@Entity(
    tableName = "articles", foreignKeys = [ForeignKey(
        entity = Feed::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("feedId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class ArticleDatabase(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "guid")
    val guid: String,

    @ColumnInfo(name = "feedId", index = true)
    val feedId: Long = 0L,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "contents")
    val contents: String,

    @ColumnInfo(name = "description")
    val description: String
)