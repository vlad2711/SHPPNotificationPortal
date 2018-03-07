package com.kram.vlad.shppnotificationportal.rest.twitter

import com.kram.vlad.shppnotificationportal.Constants
import com.kram.vlad.shppnotificationportal.rest.pojo.TwitterModels
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by vlad on 07.03.2018.
 * Twitter rest api auth
 */
interface TwitterAuth {
    @POST("oauth2/token")
    fun authorize(@Header("Authorization") base64: String,
                  @Header("Content-Type") contentType: String = "application/x-www-form-urlencoded;charset=UTF-8",
                  @Query("grant_type") body: String = "client_credentials"): Call<TwitterModels.Companion.TwitterAuth>

    companion object Factory {
        fun create(): TwitterAuth {
            val retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constants.TWITTER_BASE_URL)
                    .build()

            return retrofit.create(TwitterAuth::class.java)
        }
    }
}