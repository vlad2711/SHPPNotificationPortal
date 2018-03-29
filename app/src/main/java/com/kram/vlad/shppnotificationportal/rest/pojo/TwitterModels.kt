package com.kram.vlad.shppnotificationportal.rest.pojo

/**
 * Created by vlad on 07.03.2018.
 * Pojo models for twitter api
 */
class TwitterModels {
    companion object {
        data class TwitterAuth(val token_type: String, val access_token: String)
        data class Media(var type: String, var url: String, var previewUrl: String, val sizes: ArrayList<ImageSizes> = ArrayList())
        data class ImageSizes(val w: Int, val h: Int)
        data class Tweet constructor(val id: Long, val time: String, val text: String,
                val userName: String, val icon_url: String, val mediaUrls: ArrayList<Media> = ArrayList(), var isExpanded: Boolean = false)
    }
}