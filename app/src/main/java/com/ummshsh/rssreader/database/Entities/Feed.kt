package com.ummshsh.rssreader.database.Entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//TODO: add foreign key and cascade delete
@Entity(tableName = "feeds")
data class Feed(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "link")
    val link: String
)