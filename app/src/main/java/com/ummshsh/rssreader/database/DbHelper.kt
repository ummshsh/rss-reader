package com.ummshsh.rssreader.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

private const val SQL_CREATE_FEED_TABLE =
    "CREATE TABLE ${DatabaseContract.Feed.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${DatabaseContract.Feed.COLUMN_NAME_TITLE} TEXT," +
            "${DatabaseContract.Feed.COLUMN_NAME_LINK} TEXT)"

private const val SQL_CREATE_ARTICLE_TABLE =
    "CREATE TABLE ${DatabaseContract.Article.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY," +
            "${DatabaseContract.Article.COLUMN_NAME_FEED_ID} INTEGER," +
            "FOREIGN KEY REFERENCES (${DatabaseContract.Article.COLUMN_NAME_FEED_ID}) REFERENCES ${DatabaseContract.Feed.TABLE_NAME}(${BaseColumns._ID})," +
            "${DatabaseContract.Article.COLUMN_NAME_TITLE} TEXT," +
            "${DatabaseContract.Article.COLUMN_NAME_DESCRIPTION} TEXT"

private const val SQL_DELETE_FEED_TABLE = "DROP TABLE IF EXISTS ${DatabaseContract.Feed.TABLE_NAME}"

class DbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_FEED_TABLE)
        db?.execSQL(SQL_CREATE_ARTICLE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL(SQL_DELETE_FEED_TABLE)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FeedReader.db"
    }
}