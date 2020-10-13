package com.ummshsh.rssreader

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ummshsh.rssreader.database.FeedDatabase

class DrawerFeedListAdapter(private var listener: OnFeedClickListener) :
    RecyclerView.Adapter<DrawerFeedListAdapter.FeedHolder>() {

    var listFeeds = listOf<FeedDatabase>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = listFeeds.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.feed_item_drawer_view, parent, false)
        return FeedHolder(view)
    }

    override fun onBindViewHolder(holder: FeedHolder, position: Int) {
        val item = listFeeds[position]
        holder.title.text = item.title

        holder.title.setOnClickListener {
            listener.clickFeed(item.id)
        }
    }

    class FeedHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.feed_title)
    }

    interface OnFeedClickListener {
        fun clickFeed(id: Int)
    }
}