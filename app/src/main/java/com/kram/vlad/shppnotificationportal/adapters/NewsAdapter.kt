package com.kram.vlad.shppnotificationportal.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kram.vlad.shppnotificationportal.R
import com.kram.vlad.shppnotificationportal.Utils
import kotlinx.android.synthetic.main.news_layout_item.view.*

/**
 * Created by vlad on 09.03.2018.
 * Adapter for news recyclerView
 */
class NewsAdapter: RecyclerView.Adapter<NewsAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.news_layout_item, parent, false))

    override fun getItemCount() = Utils.news.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.text.text = Utils.news[position].text
        holder.itemView.date.text = Utils.news[position].time
        holder.itemView.name.text = Utils.news[position].userName
        holder.itemView.user_tag.text = Utils.news[position].userTag
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView)
}