package com.ummshsh.rssreader.utils

import android.util.Xml
import com.ummshsh.rssreader.model.FeedDatabase
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import java.io.IOException
import java.io.InputStream

class XmlParser {
    @Throws(XmlPullParserException::class, IOException::class)
    fun getFeed(inputStream: InputStream): FeedDatabase {
        inputStream.use { inputStream ->
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(inputStream, null)
//            parser.nextTag()
            return getFeedFromParser(parser)
        }
    }

    private fun getFeedFromParser(parser: XmlPullParser): FeedDatabase {
        val feed = FeedDatabase(-1,"","")
        TODO("TO PARSE XML and wrap to object")
    }

    fun getArticles() {
        TODO("replace external library with my own solution")
    }
}