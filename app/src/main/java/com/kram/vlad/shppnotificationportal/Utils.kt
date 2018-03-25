package com.kram.vlad.shppnotificationportal

import android.util.Base64
import com.kram.vlad.shppnotificationportal.rest.pojo.TwitterModels
import java.util.ArrayList


/**
 * Created by vlad on 06.03.2018.
 * App Utils class
 */

class Utils{
    companion object {
        var news: ArrayList<TwitterModels.Companion.Tweet> = ArrayList()
        var from = 0
        var to = 10
        fun createBase64AuthString() = "Basic " + Base64.encodeToString((Constants.TWITTER_API_KEY + ":" + Constants.TWITTER_API_SECRET)
                        .toByteArray(Charsets.UTF_8), Base64.DEFAULT)
                        .replace("\n", "")
    }
}