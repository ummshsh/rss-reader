package com.ummshsh.rssreader.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.ummshsh.rssreader.database.entities.Article
import com.ummshsh.rssreader.database.entities.Feed
import com.ummshsh.rssreader.database.entities.Folder


//TODO: split them into separate DAOs interfaces later
//TODO: to think about preferred strategies
@Dao
interface AppDatabaseDao {

    // add
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(folder: Folder): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Article): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(feed: Feed): Long

    //get
    @Query("SELECT * FROM folders WHERE id = :key")
    fun getFolder(key: Long): Folder

    @Query("SELECT * FROM articles WHERE id = :key")
    fun getArticle(key: Long): Article

    @Query("SELECT * FROM feeds WHERE id = :key")
    fun getFeed(key: Long): Feed


    //get lists
    @Query("SELECT * FROM folders")
    fun getFolders(): LiveData<List<Folder>>

    @Query("SELECT * FROM articles")
    fun getArticles(): LiveData<List<Article>>

    @Query("SELECT * FROM feeds")
    fun getFeeds(): LiveData<List<Feed>>

    // delete
    @Delete
    fun delete(article: Article): Int

    @Delete
    fun delete(folder: Folder): Int

    @Delete
    fun delete(feed: Feed): Int
}