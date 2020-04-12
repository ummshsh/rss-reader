package com.ummshsh.rssreader.database

import android.provider.BaseColumns

class DatabaseContract {
    object Article : BaseColumns {
        const val TABLE_NAME = "article"
        const val COLUMN_NAME_GUID = "guid"
        const val COLUMN_NAME_FEED_ID = "feedId"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DESCRIPTION = "description"
        const val COLUMN_NAME_CONTENTS = "contents"
        const val COLUMN_NAME_URL = "url"
    }

    object Feed : BaseColumns {
        const val TABLE_NAME = "feed"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_LINK = "link"
        const val COLUMN_NAME_FOLDER_ID = "folderId"
    }

    object Folder : BaseColumns {
        const val TABLE_NAME = "folder"
        const val COLUMN_NAME_TITLE = "title"
    }
}