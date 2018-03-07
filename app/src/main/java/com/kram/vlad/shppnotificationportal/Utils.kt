package com.kram.vlad.shppnotificationportal

import android.util.Base64


/**
 * Created by vlad on 06.03.2018.
 * App Utils class
 */

class Utils{
    companion object {
        fun createBase64AuthString() = Base64.encodeToString(
                (Constants.TWITTER_API_KEY + ":" + Constants.TWITTER_API_SECRET).toByteArray(Charsets.UTF_8), Base64.DEFAULT)
                .replace("\n", "")
    }
}