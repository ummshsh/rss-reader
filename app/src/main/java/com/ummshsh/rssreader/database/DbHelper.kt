package com.ummshsh.rssreader.database

import android.content.ContentValues
import android.content.Context
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

private const val SQL_CREATE_FEED_TABLE =
    "CREATE TABLE ${DatabaseContract.Feed.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
            "${DatabaseContract.Feed.COLUMN_NAME_TITLE} TEXT," +
            "${DatabaseContract.Feed.COLUMN_NAME_LINK} TEXT)"

private const val SQL_CREATE_ARTICLE_TABLE =
    "CREATE TABLE ${DatabaseContract.Article.TABLE_NAME} (" +
            "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "${DatabaseContract.Article.COLUMN_NAME_GUID} TEXT, " +
            "${DatabaseContract.Article.COLUMN_NAME_FEED_ID} INTEGER, " +
            "${DatabaseContract.Article.COLUMN_NAME_TITLE} TEXT, " +
            "${DatabaseContract.Article.COLUMN_NAME_CONTENTS} TEXT, " +
            "${DatabaseContract.Article.COLUMN_NAME_DESCRIPTION} TEXT, " +
            "${DatabaseContract.Article.COLUMN_NAME_URL} TEXT, " +
            "FOREIGN KEY (${DatabaseContract.Article.COLUMN_NAME_FEED_ID}) REFERENCES ${DatabaseContract.Feed.TABLE_NAME}(${BaseColumns._ID}))"

private const val SQL_DELETE_FEED_TABLE = "DROP TABLE IF EXISTS ${DatabaseContract.Feed.TABLE_NAME}"

class DbHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
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

    override fun onConfigure(db: SQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
        super.onConfigure(db)
    }

    fun getFeeds(): List<Feed> {
        val cursor = readableDatabase.query(
            DatabaseContract.Feed.TABLE_NAME,
            arrayOf(
                BaseColumns._ID,
                DatabaseContract.Feed.COLUMN_NAME_TITLE,
                DatabaseContract.Feed.COLUMN_NAME_LINK
            ),
            null, null, null, null, null
        )

        val feeds = mutableListOf<Feed>()
        with(cursor) {
            while (moveToNext()) {
                val itemId = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                val title =
                    getString(getColumnIndexOrThrow(DatabaseContract.Feed.COLUMN_NAME_TITLE))
                val link = getString(getColumnIndexOrThrow(DatabaseContract.Feed.COLUMN_NAME_LINK))
                feeds.add(Feed(itemId, title, link, 0))
            }
        }

        return feeds
    }

    fun insert(articles: List<ArticleDatabase>) {
        articles.forEach {
            val values = ContentValues().apply {
                put(DatabaseContract.Article.COLUMN_NAME_GUID, it.guid)
                put(DatabaseContract.Article.COLUMN_NAME_FEED_ID, it.feedId)
                put(DatabaseContract.Article.COLUMN_NAME_TITLE, it.title)
                put(
                    DatabaseContract.Article.COLUMN_NAME_CONTENTS,
                    DatabaseUtils.sqlEscapeString(it.contents)
                )
                put(DatabaseContract.Article.COLUMN_NAME_DESCRIPTION, it.description)
                put(DatabaseContract.Article.COLUMN_NAME_URL, it.url)
            }
            writableDatabase
                .insert(DatabaseContract.Article.TABLE_NAME, null, values)
        }
    }

    fun getArticles(): List<ArticleDatabase> {
        val cursor = readableDatabase.query(
            DatabaseContract.Article.TABLE_NAME,
            arrayOf(
                BaseColumns._ID,
                DatabaseContract.Article.COLUMN_NAME_GUID,
                DatabaseContract.Article.COLUMN_NAME_FEED_ID,
                DatabaseContract.Article.COLUMN_NAME_TITLE,
                DatabaseContract.Article.COLUMN_NAME_CONTENTS,
                DatabaseContract.Article.COLUMN_NAME_DESCRIPTION,
                DatabaseContract.Article.COLUMN_NAME_URL
            ),
            null, null, null, null, null
        )

        val articles = mutableListOf<ArticleDatabase>()
        with(cursor) {
            while (moveToNext()) {
                val itemId = getInt(getColumnIndexOrThrow(BaseColumns._ID))
                val guid =
                    getString(getColumnIndexOrThrow(DatabaseContract.Article.COLUMN_NAME_GUID))
                val feedId =
                    getInt(getColumnIndexOrThrow(DatabaseContract.Article.COLUMN_NAME_FEED_ID))
                val title =
                    getString(getColumnIndexOrThrow(DatabaseContract.Article.COLUMN_NAME_TITLE))
                val contents =
                    getString(getColumnIndexOrThrow(DatabaseContract.Article.COLUMN_NAME_CONTENTS))
                val description =
                    getString(getColumnIndexOrThrow(DatabaseContract.Article.COLUMN_NAME_DESCRIPTION))
                val url =
                    getString(getColumnIndexOrThrow(DatabaseContract.Article.COLUMN_NAME_URL))

                articles.add(
                    ArticleDatabase(
                        itemId,
                        guid,
                        feedId,
                        title,
                        contents,
                        description,
                        url
                    )
                )
            }
        }

        return articles
    }

    fun getArticlesWithGuids(guids: List<String>): List<String> {
        val stringBuilder = StringBuilder()
        guids.forEach { _ -> stringBuilder.append("${DatabaseContract.Article.COLUMN_NAME_GUID} = ? OR ") }
        val where = stringBuilder.toString().trimEnd('O', 'R', ' ')


        val cursor = readableDatabase.query(
            DatabaseContract.Article.TABLE_NAME,
            arrayOf(
                BaseColumns._ID,
                DatabaseContract.Article.COLUMN_NAME_GUID
            ),
            where,
            guids.toTypedArray(),
            null,
            null,
            null
        )


        val foundArticleGuids = mutableListOf<String>()
        with(cursor) {
            while (moveToNext()) {
                val guid =
                    getString(getColumnIndexOrThrow(DatabaseContract.Article.COLUMN_NAME_GUID))
                foundArticleGuids.add(guid)
            }
        }

        return foundArticleGuids
    }

    fun insert(vararg feeds: Feed) {
        feeds.forEach {
            val values = ContentValues().apply {
                put(DatabaseContract.Feed.COLUMN_NAME_TITLE, it.title)
                put(DatabaseContract.Feed.COLUMN_NAME_LINK, it.link)
            }
            writableDatabase
                .insert(DatabaseContract.Feed.TABLE_NAME, null, values)
        }
    }

    fun deleteFeed(id: Int) {
        // Delete feeds first
        writableDatabase.delete(
            DatabaseContract.Article.TABLE_NAME,
            "${DatabaseContract.Article.COLUMN_NAME_FEED_ID}=?",
            arrayOf(id.toString())
        )

        // Delete folder
        writableDatabase
            .delete(
                DatabaseContract.Feed.TABLE_NAME,
                "${BaseColumns._ID} = ?",
                arrayOf(id.toString())
            )
    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FeedReader.db"
    }
}