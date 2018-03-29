package com.kram.vlad.shppnotificationportal.utils

import android.content.Context
import com.kram.vlad.shppnotificationportal.Constants
import com.kram.vlad.shppnotificationportal.rest.pojo.TwitterModels
import com.kram.vlad.shppnotificationportal.rest.twitter.GetTweets
import com.kram.vlad.shppnotificationportal.rest.twitter.GetTweetsMaxId
import com.kram.vlad.shppnotificationportal.rest.twitter.TwitterAuth
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by vlad on 26.03.2018.
 * Utils for rest
 */
class RESTUtils(val context: Context) {
    var authKey = ""
    fun getTweets(callback: Callback<ResponseBody>) {
        if (context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).contains("token")) {
            authKey = "Bearer " + context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString("token", "")
            GetTweets.create(context).getTweets(authKey).enqueue(callback)
        } else
            TwitterAuth.create().authorize(Utils.createBase64AuthString()).enqueue(object : Callback<TwitterModels.Companion.TwitterAuth> {
                override fun onFailure(call: Call<TwitterModels.Companion.TwitterAuth>?, t: Throwable?) = t!!.printStackTrace()
                override fun onResponse(call: Call<TwitterModels.Companion.TwitterAuth>?, response: Response<TwitterModels.Companion.TwitterAuth>?) {
                    authKey = "Bearer " + response!!.body()!!.access_token
                    context.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putString("token", response.body()!!.access_token).apply()
                    GetTweets.create(context).getTweets(authKey).enqueue(callback)}})
    }

    fun getTweetsMaxId(maxId: Long, callback: Callback<ResponseBody>) = GetTweetsMaxId.create(context).getTweetsMaxId(authKey, maxId).enqueue(callback)
}

