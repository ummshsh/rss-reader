package com.ummshsh.rssreader.network

import com.ummshsh.rssreader.model.FeedDatabase
import com.ummshsh.rssreader.utils.XmlParser
import okhttp3.*
import java.io.IOException

class FeedFinder {
    fun findFeed(url: String): FeedDatabase {
        var feed = FeedDatabase.EmptyFeed

        var client = OkHttpClient()
        var request = Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                feed = FeedDatabase.EmptyFeed
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    var stream = response.body()?.byteStream()
                    if (stream != null) {
                        feed = XmlParser().getFeed(stream)
                    }
                }
            }
        })

        return feed
    }
}