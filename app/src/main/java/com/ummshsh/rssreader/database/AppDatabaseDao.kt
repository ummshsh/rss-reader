package com.ummshsh.rssreader.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ummshsh.rssreader.database.Entities.Article
import com.ummshsh.rssreader.database.Entities.Feed
import com.ummshsh.rssreader.database.Entities.Folder


//TODO: split them into separate DAOs interfaces later
//TODO: to think about preferred strategies
@Dao
interface AppDatabaseDao {

    // add
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(folder: Folder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(feed: Feed)

    //get
    @Query("SELECT * FROM folders WHERE id = :key")
    fun getFolder(key: Long): Folder

    @Query("SELECT * FROM articles WHERE id = :key")
    fun getArticle(key: Long): Article

    @Query("SELECT * FROM feeds WHERE id = :key")
    fun getFeed(key: Long): Feed


    //get lists
    @Query("SELECT * FROM folders")
    fun getFolders(key: Long): LiveData<List<Folder>>

    @Query("SELECT * FROM articles")
    fun getArticles(key: Long): LiveData<List<Article>>

    @Query("SELECT * FROM feeds")
    fun getFeeds(key: Long): LiveData<List<Feed>>
}