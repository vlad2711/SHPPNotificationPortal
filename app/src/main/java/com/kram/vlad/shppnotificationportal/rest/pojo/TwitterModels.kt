package com.kram.vlad.shppnotificationportal.rest.pojo

/**
 * Created by vlad on 07.03.2018.
 * Pojo models for twitter api
 */
class TwitterModels {
    companion object {
        data class TwitterAuth(val token_type: String, val access_token: String)
        @JvmSuppressWildcards data class Media( @JvmSuppressWildcards var type: String, @JvmSuppressWildcards var url: String, @JvmSuppressWildcards var previewUrl: String, @JvmSuppressWildcards val sizes: ArrayList<ImageSizes> = ArrayList())
        data class ImageSizes(val w: Int, val h: Int)
        data class Tweet constructor(val id: Long, val time: String, val text: String,
                val userName: String, val icon_url: String,   val mediaUrls: ArrayList<Media> = ArrayList(), var isExpanded: Boolean = false)
    }
}