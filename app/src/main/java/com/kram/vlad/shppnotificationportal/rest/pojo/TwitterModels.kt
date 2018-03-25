package com.kram.vlad.shppnotificationportal.rest.pojo

import android.graphics.Bitmap

/**
 * Created by vlad on 07.03.2018.
 * Pojo models for twitter api
 */
class TwitterModels {
    companion object {
        data class TwitterAuth(val token_type: String, val access_token: String)
        data class Media(var type: String, var url: String)
        data class Tweet @JvmOverloads constructor(val id: Long,
                val time: String,
                val text: String,
                val userName: String,
                val icon_url: String,
                val mediaUrls: List<Media> = List(0, { Media("", "")}))
    }
}