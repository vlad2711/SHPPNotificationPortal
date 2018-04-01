package com.kram.vlad.shppnotificationportal.adapters

import android.content.Intent
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kram.vlad.shppnotificationportal.R
import com.kram.vlad.shppnotificationportal.activitys.MainActivity
import com.kram.vlad.shppnotificationportal.activitys.MediaActivity
import com.kram.vlad.shppnotificationportal.utils.Utils
import kotlinx.android.synthetic.main.news_layout_item.view.*
import java.text.DateFormat
import java.util.*

/**
 * Created by vlad on 09.03.2018.
 * Adapter for news recyclerView
 */
class NewsAdapter : RecyclerView.Adapter<NewsAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.news_layout_item, parent, false))
    override fun getItemCount() = Utils.news.size
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(position)
    }

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        private val TAG = this::class.java.simpleName

        fun bind(position: Int) {
            if (!Utils.news[position].isExpanded) {
                itemView.expandable.collapse()
            } else {
                for (i in 0 until Utils.news[position].mediaUrls.size) {
                    ((itemView.expandable.getChildAt(0) as ConstraintLayout).getChildAt(i) as ImageView).setImageUrl(Utils.news[position].mediaUrls[i].url)
                }

                itemView.expandable.expand()
            }

            val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT).format(Date(Utils.news[position].time))
            itemView.text.text = Utils.news[position].text
            itemView.date.text = dateFormat
            itemView.name.text = Utils.news[position].userName
            itemView.icon.setImageUrl(Utils.news[position].icon_url)

            if (Utils.news[position].mediaUrls.isNotEmpty()) {
                itemView.media_type_text.text = itemView.resources.getString(Utils.getTextByType(Utils.news[position].mediaUrls[0].type))
                itemView.media_type_icon.setImageResource(Utils.getImageByType(Utils.news[position].mediaUrls[0].type))
                itemView.media_content.visibility = View.VISIBLE
                if (Utils.news[position].mediaUrls[0].type == "photo") {
                    itemView.media_content.setOnClickListener {
                        for (i in 0 until Utils.news[position].mediaUrls.size) {
                            ((itemView.expandable.getChildAt(0) as ConstraintLayout).getChildAt(i) as ImageView).setImageUrl(Utils.news[position].mediaUrls[i].url)
                        }

                        if (itemView.expandable.isExpanded) {
                            itemView.expandable.collapse()
                        } else {
                            itemView.expandable.expand()
                        }

                        Utils.news[position].isExpanded = itemView.expandable.isExpanded
                    }
                } else{
                    itemView.media_content.setOnClickListener({(itemView.context as MainActivity).startActivity(
                            Intent(itemView.context, MediaActivity::class.java).apply { putExtra("position", position)}
                    )})
                }
            } else {
                itemView.media_content.visibility = View.INVISIBLE
            }

            if (position + 3 == Utils.news.size && Utils.news.size == Utils.newsBufSize) {
                (itemView.context as MainActivity).getTweetsMaxId(Utils.news[Utils.news.size - 1].id)
                Utils.newsBufSize += 10
            }
        }

        private fun ImageView.setImageUrl(url: String) = Glide.with(this).load(Uri.parse(url))
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(Target.SIZE_ORIGINAL)).into(this)
    }
}