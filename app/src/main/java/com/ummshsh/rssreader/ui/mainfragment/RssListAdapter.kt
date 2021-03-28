package com.ummshsh.rssreader.ui.mainfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ummshsh.rssreader.R
import com.ummshsh.rssreader.model.ArticleLight

class RssListAdapter(private val clickListener: OnArticleClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val typeRead = 1
    private val typeUnread = 2

    var listArticles = listOf<ArticleLight>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = listArticles.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return if (viewType == typeRead) {
            RssHolderRead(layoutInflater.inflate(R.layout.rss_item_view_read, parent, false))
        } else {
            RssHolder(layoutInflater.inflate(R.layout.rss_item_view, parent, false))
        }
    }

    override fun getItemViewType(position: Int): Int=
        if (listArticles[position].isRead) typeRead else typeUnread

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = listArticles[position]

        if (getItemViewType(position) == typeRead) {
            var read = holder as RssHolderRead

            read.title.text = item.title
            read.source.text = item.feedId.toString()
            read.content.text =item.contentsShort
            read.link.text = item.url

            read.title.setOnClickListener {
                clickListener.clickOnArticle(item.id)
            }
        } else {
            var unread = holder as RssHolder

            unread.title.text = item.title
            unread.source.text = item.feedId.toString()
            unread.content.text =item.contentsShort
            unread.link.text = item.url

            unread.title.setOnClickListener {
                clickListener.clickOnArticle(item.id)
            }
        }

    }

    class RssHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.rss_title)
        val source: TextView = itemView.findViewById(R.id.rss_source)
        val content: TextView = itemView.findViewById(R.id.rss_contents)
        val link: TextView = itemView.findViewById(R.id.rss_link)
    }

    class RssHolderRead(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.rss_title)
        val source: TextView = itemView.findViewById(R.id.rss_source)
        val content: TextView = itemView.findViewById(R.id.rss_contents)
        val link: TextView = itemView.findViewById(R.id.rss_link)
    }

    interface OnArticleClickListener {
        fun clickOnArticle(id: Int)
    }
}