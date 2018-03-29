package com.kram.vlad.shppnotificationportal.rest.twitter

import android.content.Context
import com.kram.vlad.shppnotificationportal.Constants
import okhttp3.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

/**
 * Created by vlad on 26.03.2018.
 * Retrofit get interface. Get tweets later than id
 */
interface GetTweetsMaxId {
    @GET("1.1/statuses/user_timeline.json")
    fun getTweetsMaxId (@Header("Authorization") bearer: String,
                          @Query("max_id") to: Long,
                          @Query("exclude_replies") exclude: Boolean = true,
                          @Query("screen_name") name: String = Constants.TWITTER_SHPP_NAME,
                          @Query("count") count: Int = 10): Call<ResponseBody>
    companion object Factory{
        private val TAG = this::class.java.simpleName

        fun create(context: Context): GetTweetsMaxId{
            val client = OkHttpClient.Builder().cache(Cache(context.cacheDir, 10 * 1024 * 1024)).build()
            val retrofit = Retrofit.Builder()
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constants.TWITTER_BASE_URL)
                    .build()

            return retrofit.create(GetTweetsMaxId::class.java)
        }
    }
}