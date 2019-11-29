package com.ummshsh.rssreader.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ummshsh.rssreader.database.entities.Article
import com.ummshsh.rssreader.database.entities.Feed
import com.ummshsh.rssreader.database.entities.Folder


//TODO: split them into separate DAOs interfaces later
//TODO: to think about preferred strategies
@Dao
interface AppDatabaseDao {

    // add
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(folder: Folder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(article: Article)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(feed: Feed)

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
}