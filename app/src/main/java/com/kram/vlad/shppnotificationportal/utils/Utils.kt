package com.kram.vlad.shppnotificationportal.utils

import android.graphics.Bitmap
import android.util.Base64
import com.kram.vlad.shppnotificationportal.Constants
import com.kram.vlad.shppnotificationportal.R
import com.kram.vlad.shppnotificationportal.rest.pojo.TwitterModels
import java.util.ArrayList


/**
 * Created by vlad on 06.03.2018.
 * App Utils class
 */

class Utils{
    companion object {
        var newsBufSize = 0
        var news: ArrayList<TwitterModels.Companion.Tweet> = ArrayList()

        fun createBase64AuthString() = "Basic " + Base64.encodeToString((Constants.TWITTER_API_KEY + ":" + Constants.TWITTER_API_SECRET)
                        .toByteArray(Charsets.UTF_8), Base64.DEFAULT)
                        .replace("\n", "")

        fun getImageByType(type: String) = when(type){
            "photo" -> R.drawable.image_icon
            else -> R.drawable.video_icon
        }

        fun getTextByType(type: String) = when(type){
            "photo" -> R.string.photo
            "video" -> R.string.video
            else -> R.string.gif
        }
    }
}