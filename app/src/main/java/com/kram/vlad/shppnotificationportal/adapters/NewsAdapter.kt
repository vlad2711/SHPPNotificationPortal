package com.kram.vlad.shppnotificationportal.adapters

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
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

    var position:Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.news_layout_item, parent, false))
    override fun getItemCount() = Utils.news.size
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bind(position)
        this.position = position
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
                        } else{
                            itemView.expandable.expand()
                        }

                        Utils.news[position].isExpanded = itemView.expandable.isExpanded

                        Log.d(TAG, "expand")
                    }
                } else{
                    itemView.media_content.setOnClickListener({(itemView.context as MainActivity)
                            .startActivity(Intent(itemView.context, MediaActivity::class.java).apply { putExtra("position", position)})
                    })
                }
            } else {
                itemView.media_content.visibility = View.INVISIBLE
            }

            if(Utils.news[position].urls.isNotEmpty()){
                val urls = Utils.news[position].urls
                var text = Utils.news[position].text

                for (j in 0 until urls.size) text = text.replace(urls[j].url, " <a href='${urls[j].expandedUrl}'>${urls[j].displayUrl}</a>")

                itemView.text.movementMethod = LinkMovementMethod.getInstance()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    itemView.text.text = Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
                } else{
                    itemView.text.text = Html.fromHtml(text)
                }
            }

            if (position + 3 == Utils.news.size && Utils.news.size == Utils.newsBufSize && position != 0) {
                (itemView.context as MainActivity).getTweetsMaxId(Utils.news.last().id -1)
                Utils.newsBufSize += 10
            }
        }

        private fun ImageView.setImageUrl(url: String) = Glide.with(this).load(Uri.parse(url))
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.DATA).override(Target.SIZE_ORIGINAL)).into(this)
    }
}