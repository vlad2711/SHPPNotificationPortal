package com.kram.vlad.shppnotificationportal.rest.pojo

/**
 * Created by vlad on 07.03.2018.
 */
class TwitterModels {
    companion object {
        data class TwitterAuth(val token_type: String, val access_token: String)
    }
}