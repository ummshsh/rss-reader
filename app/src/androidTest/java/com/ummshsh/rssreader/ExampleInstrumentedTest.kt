package com.ummshsh.rssreader

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ummshsh.rssreader.database.*
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private lateinit var context: Context
    private lateinit var userArticleDao: AppDatabaseArticleDao
    private lateinit var db: AppDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()

        userArticleDao = db.appDatabaseArticleDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun checkCascadeDelete() {
        // Create folder
        val folder = Folder(0, "Default folder")
        var folderId = userArticleDao.insert(folder)
        userArticleDao.getFolders().observeOnce {
            assertThat(it.first()?.name, equalTo(folder.name))
        }

        // Create feed
        val feed = Feed(0, "Fancy feed", "www.www.www", folderId)
        var feedId = userArticleDao.insert(feed)
        userArticleDao.getFeeds().observeOnce {
            assertThat(it.first()?.title, equalTo(feed.title))
        }

        // Create article
        val article = ArticleDatabase(
            0,
            "guid123",
            feedId,
            "Fancy article",
            "Contents of the article",
            "Concise descripton"
        )
        userArticleDao.insert(article)
        userArticleDao.getArticles().observeOnce {
            assertThat(it.first()?.title, equalTo(article.title))
        }

        // Delete the folder
//        var deletedItemsCount = userDao.delete(folder)
//        assertThat(deletedItemsCount, equalTo(1))
//
//        // Check that other entities deleted as well
//        userDao.getFolders().observeOnce {
//            assertThat(it.size, equalTo(0))
//        }
//        userDao.getFeeds().observeOnce {
//            assertThat(it.size, equalTo(0))
//        }
//        userDao.getArticles().observeOnce {
//            assertThat(it.size, equalTo(0))
//        }
    }
}
