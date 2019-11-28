package com.ummshsh.rssreader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prof.rssparser.Article

class RssListAdapter : RecyclerView.Adapter<RssListAdapter.RssHolder>() {

    var listArticles = listOf<Article>()
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
        holder.contents.text = item.content.toString() // TODO: just title for now
    }

    class RssHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contents: TextView = itemView.findViewById(R.id.rss_item_text)
    }
}