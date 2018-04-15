package com.kram.vlad.shppnotificationportal.rest.twitter

import android.content.Context
import com.kram.vlad.shppnotificationportal.Constants
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by vlad on 07.03.2018.
 * Return tweets from twitter rest api
 */
interface GetTweets {
    @GET("1.1/statuses/user_timeline.json")
    fun getTweets (@Header("Authorization") bearer: String,
                   @Query("exclude_replies") exclude: Boolean = true,
                   @Query("screen_name") name: String = Constants.TWITTER_SHPP_NAME,
                   @Query("count") count: Int = 10,
                   @Query("tweet_mode") mode:String = "extended"): Call<ResponseBody>

    companion object Factory {
        fun create(): GetTweets {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constants.TWITTER_BASE_URL)
                    .build()

            return retrofit.create(GetTweets::class.java)
        }
    }
}