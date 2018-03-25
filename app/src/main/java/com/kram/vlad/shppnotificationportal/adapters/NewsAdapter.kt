package com.kram.vlad.shppnotificationportal.adapters

import android.net.Uri
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
import com.kram.vlad.shppnotificationportal.Utils
import com.kram.vlad.shppnotificationportal.activitys.MainActivity
import kotlinx.android.synthetic.main.four_images_fragment.view.*
import kotlinx.android.synthetic.main.news_layout_item.view.*


/**
 * Created by vlad on 09.03.2018.
 * Adapter for news recyclerView
 */
class NewsAdapter : RecyclerView.Adapter<NewsAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(LayoutInflater.from(parent.context).inflate(R.layout.news_layout_item, parent, false))
    override fun getItemCount() = Utils.news.size
    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(position)

    class Holder(itemView: View?) : RecyclerView.ViewHolder(itemView){
        private val TAG = this::class.java.simpleName

        fun bind(position: Int){
            itemView.text.text = Utils.news[position].text
            itemView.date.text = Utils.news[position].time
            itemView.name.text = Utils.news[position].userName
            itemView.icon.setImageUrl(Utils.news[position].icon_url)

            if(!Utils.news[position].mediaUrls.isEmpty()){
                val viewGroup: ViewGroup
                if(itemView.findViewById<ImageView>(R.id.image1) == null) {
                    val layout = getLayout(Utils.news[position].mediaUrls.size)
                    viewGroup = ((itemView.context as MainActivity).layoutInflater.inflate(layout, itemView.media_content) as ViewGroup).getChildAt(0) as ViewGroup
                } else{
                    viewGroup = itemView.media_content.getChildAt(0) as ViewGroup
                }
                for (i in 0 until viewGroup.childCount) (viewGroup.getChildAt(i) as ImageView).setImageUrl(Utils.news[position].mediaUrls[0].url)
            } else{
                if(itemView.findViewById<ImageView>(R.id.image1) != null) Glide.with(itemView.image1).clear(itemView.image1)
            }

            //if(position + 3 == Utils.news.size) {
               // (itemView.context as MainActivity).getTweets()
              //  Utils.from = Utils.to
              ///  Utils.to = Utils.from + 10
           // }

        }

        private fun ImageView.setImageUrl(url: String) = Glide.with(this).load(Uri.parse(url))
                .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(Target.SIZE_ORIGINAL)).into(this)


        private fun getLayout(countOfImages: Int) =
             when(countOfImages){
                1 -> R.layout.one_image_fragment
                2 -> R.layout.two_image_fragment
                3 -> R.layout.three_images_fragment
                4 -> R.layout.four_images_fragment
                else -> 0
            }

    }
}