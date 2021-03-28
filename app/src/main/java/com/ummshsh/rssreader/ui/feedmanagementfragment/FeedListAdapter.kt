package com.ummshsh.rssreader.ui.feedmanagementfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ummshsh.rssreader.R
import com.ummshsh.rssreader.model.FeedDatabase


class FeedListAdapter(private var listener: OnFeedDeleteClickListener) :
    RecyclerView.Adapter<FeedListAdapter.FeedHolder>() {

    var listFeeds = listOf<FeedDatabase>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = listFeeds.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.feed_item_view, parent, false)
        return FeedHolder(view)
    }

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {
        val item = listFeeds[position]
        holder.title.text = item.title
        holder.url.text = item.link

        holder.deleteButton.setOnClickListener {
            listener.clickDeleteOnItem(item.id)
        }
    }

    class FeedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.feed_title)
        val url: TextView = itemView.findViewById(R.id.feed_url)
        var deleteButton: Button = itemView.findViewById(R.id.delete_feed)
    }

    interface OnFeedDeleteClickListener {
        fun clickDeleteOnItem(id: Int)
    }
}