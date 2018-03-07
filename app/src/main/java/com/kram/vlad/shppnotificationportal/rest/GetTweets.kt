package com.kram.vlad.shppnotificationportal.rest

import retrofit2.http.GET

/**
 * Created by vlad on 07.03.2018.
 */
interface GetTweets {

    @GET()
    fun getTweets ()
}