package com.ummshsh.rssreader.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ummshsh.rssreader.R
import com.ummshsh.rssreader.database.ArticleDatabase

class RssListAdapter : RecyclerView.Adapter<RssListAdapter.RssHolder>() {

    var listArticles = listOf<ArticleDatabase>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = listArticles.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RssHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.rss_item_view, parent, false)
        return RssHolder(view)
    }

    override fun onBindViewHolder(holder: RssHolder, position: Int) {
        val item = listArticles[position]
        holder.title.text = item.title
        holder.content.text = item.contents.subSequence(0, 30)
        holder.link.text = item.url
    }

    class RssHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.rss_title)
        val content: TextView = itemView.findViewById(R.id.rss_contents)
        val link: TextView = itemView.findViewById(R.id.rss_link)
    }
}