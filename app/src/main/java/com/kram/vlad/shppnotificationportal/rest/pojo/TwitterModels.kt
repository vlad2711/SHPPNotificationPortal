package com.kram.vlad.shppnotificationportal.rest.pojo

/**
 * Created by vlad on 07.03.2018.
 * Pojo models for twitter api
 */
class TwitterModels {
    companion object {
        data class TwitterAuth(val token_type: String, val access_token: String)
        data class Tweet(val time: String, val text: String, val userName: String, val userTag: String, val icon_url: String)
    }
}