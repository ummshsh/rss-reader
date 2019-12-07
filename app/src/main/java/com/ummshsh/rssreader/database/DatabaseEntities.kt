package com.ummshsh.rssreader.database

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

@Entity(
    tableName = "feeds", foreignKeys = [ForeignKey(
        entity = Folder::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("folderId"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Feed(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "link")
    val link: String,

    @ColumnInfo(name = "folderId", index = true)
    val folderId: Long
)


@Entity(tableName = "folders")
data class Folder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "name")
    val name: String
)