package com.ummshsh.rssreader.database

import androidx.lifecycle.LiveData
import androidx.room.*

//TODO: to think about preferred strategies

@Dao
interface AppDatabaseArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articleDatabase: ArticleDatabase): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(articlesDatabase: List<ArticleDatabase>): List<Long>

    @Query("SELECT * FROM articles WHERE id = :key")
    fun getArticle(key: Long): ArticleDatabase

    @Query("SELECT * FROM articles")
    fun getArticles(): LiveData<List<ArticleDatabase>>

    @Delete
    fun delete(articleDatabase: ArticleDatabase): Int
}

@Dao
interface AppDatabaseFolderDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(folder: Folder): Long

    @Query("SELECT * FROM folders")
    fun getFolders(): LiveData<List<Folder>>

    @Delete
    fun delete(folder: Folder): Int

    @Query("SELECT * FROM folders WHERE id = :key")
    fun getFolder(key: Long): Folder
}

@Dao
interface AppDatabaseFeedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(feed: Feed): Long

    @Query("SELECT * FROM feeds WHERE id = :key")
    fun getFeed(key: Long): Feed

    @Query("SELECT * FROM feeds")
    fun getFeeds(): LiveData<List<Feed>>

    @Delete
    fun delete(feed: Feed): Int
}
