package com.ummshsh.rssreader.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.ummshsh.rssreader.model.ArticleStatus

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
            "${DatabaseContract.Article.COLUMN_NAME_READ} BOOLEAN NOT NULL CHECK (${DatabaseContract.Article.COLUMN_NAME_READ} IN (0,1)), " +
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
                put(DatabaseContract.Article.COLUMN_NAME_CONTENTS, it.contents)
                put(DatabaseContract.Article.COLUMN_NAME_DESCRIPTION, it.description)
                put(DatabaseContract.Article.COLUMN_NAME_URL, it.url)
                put(DatabaseContract.Article.COLUMN_NAME_READ, it.isRead)
            }
            writableDatabase
                .insert(DatabaseContract.Article.TABLE_NAME, null, values)
        }
    }

    fun getArticles(status: ArticleStatus, ascending: Boolean, feedId: Int): List<ArticleDatabase> {
        val selectionReadUnread = when (status) {
            ArticleStatus.All -> ""
            ArticleStatus.Read -> "${DatabaseContract.Article.COLUMN_NAME_READ} = 1"
            ArticleStatus.Unread -> "${DatabaseContract.Article.COLUMN_NAME_READ} = 0"
        }

        val selectionFeed =
            if (feedId < 0) "" else "${DatabaseContract.Article.COLUMN_NAME_FEED_ID} = $feedId"

        var fullSelection =
            arrayOf(selectionReadUnread, selectionFeed).filter { it.isNotEmpty() }
                .joinToString(separator = " AND ")

        val orderBy = "${BaseColumns._ID} " + if (ascending) "ASC" else "DESC"

        val cursor = readableDatabase.query(
            DatabaseContract.Article.TABLE_NAME,
            arrayOf(
                BaseColumns._ID,
                DatabaseContract.Article.COLUMN_NAME_GUID,
                DatabaseContract.Article.COLUMN_NAME_FEED_ID,
                DatabaseContract.Article.COLUMN_NAME_TITLE,
                DatabaseContract.Article.COLUMN_NAME_CONTENTS,
                DatabaseContract.Article.COLUMN_NAME_DESCRIPTION,
                DatabaseContract.Article.COLUMN_NAME_URL,
                DatabaseContract.Article.COLUMN_NAME_READ
            ),
            fullSelection,
            null,
            null,
            null,
            orderBy
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
                val read =
                    getInt(getColumnIndexOrThrow(DatabaseContract.Article.COLUMN_NAME_READ))

                articles.add(
                    ArticleDatabase(
                        itemId,
                        guid,
                        feedId,
                        title,
                        contents,
                        description,
                        url,
                        read > 0
                    )
                )
            }
        }

        return articles
    }

    fun getArticlesWithGuids(guidList: List<String>): List<String> {
        val stringBuilder = StringBuilder()
        repeat(guidList.size) {
            stringBuilder.append("${DatabaseContract.Article.COLUMN_NAME_GUID} = ? OR ")
        }
        val where = stringBuilder.toString().trimEnd('O', 'R', ' ')


        val cursor = readableDatabase.query(
            DatabaseContract.Article.TABLE_NAME,
            arrayOf(
                BaseColumns._ID,
                DatabaseContract.Article.COLUMN_NAME_GUID
            ),
            where,
            guidList.toTypedArray(),
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

    fun getArticle(articleId: Int): ArticleDatabase {
        lateinit var article: ArticleDatabase
        var cursor = readableDatabase.query(
            DatabaseContract.Article.TABLE_NAME,
            arrayOf(
                DatabaseContract.Article.COLUMN_NAME_GUID,
                DatabaseContract.Article.COLUMN_NAME_FEED_ID,
                DatabaseContract.Article.COLUMN_NAME_TITLE,
                DatabaseContract.Article.COLUMN_NAME_CONTENTS,
                DatabaseContract.Article.COLUMN_NAME_DESCRIPTION,
                DatabaseContract.Article.COLUMN_NAME_URL
            ),
            "${BaseColumns._ID} = ?",
            arrayOf(articleId.toString()),
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                article = ArticleDatabase(
                    articleId,
                    getString(getColumnIndexOrThrow(DatabaseContract.Article.COLUMN_NAME_GUID)),
                    getInt(getColumnIndexOrThrow(DatabaseContract.Article.COLUMN_NAME_FEED_ID)),
                    getString(getColumnIndexOrThrow(DatabaseContract.Article.COLUMN_NAME_TITLE)),
                    getString(getColumnIndexOrThrow(DatabaseContract.Article.COLUMN_NAME_CONTENTS)),
                    getString(getColumnIndexOrThrow(DatabaseContract.Article.COLUMN_NAME_DESCRIPTION)),
                    getString(getColumnIndexOrThrow(DatabaseContract.Article.COLUMN_NAME_URL))
                )
            }
        }
        return article
    }

    fun getFeedName(feedId: Int): String {
        var feedName = ""
        var cursor = readableDatabase.query(
            DatabaseContract.Feed.TABLE_NAME,
            arrayOf(DatabaseContract.Feed.COLUMN_NAME_TITLE),
            "${BaseColumns._ID} = ?",
            arrayOf(feedId.toString()),
            null,
            null,
            null
        )

        with(cursor) {
            while (moveToNext()) {
                feedName = getString(getColumnIndexOrThrow(DatabaseContract.Feed.COLUMN_NAME_TITLE))
            }
        }
        return feedName
    }

    fun markArticleAsRead(isRead: Boolean, vararg articleIds: Int) {
        articleIds.forEach {
            val values = ContentValues().apply {
                put(DatabaseContract.Article.COLUMN_NAME_READ, isRead)
            }
            writableDatabase.update(
                DatabaseContract.Article.TABLE_NAME,
                values,
                "${BaseColumns._ID} = ?",
                arrayOf(it.toString())
            )
        }

    }

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "FeedReader.db"
    }
}