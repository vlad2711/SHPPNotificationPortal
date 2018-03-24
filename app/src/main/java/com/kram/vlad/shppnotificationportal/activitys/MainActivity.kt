package com.kram.vlad.shppnotificationportal.activitys

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.kram.vlad.shppnotificationportal.Constants
import com.kram.vlad.shppnotificationportal.R
import com.kram.vlad.shppnotificationportal.Utils
import com.kram.vlad.shppnotificationportal.adapters.NewsAdapter
import com.kram.vlad.shppnotificationportal.rest.pojo.TwitterModels
import com.kram.vlad.shppnotificationportal.rest.twitter.GetTweets
import com.kram.vlad.shppnotificationportal.rest.twitter.TwitterAuth
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), Callback<ResponseBody> {

    private val TAG = this::class.java.simpleName

    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) = t!!.printStackTrace()

    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
        val respon = JSONArray(response!!.body()!!.string())
        for (i in 0 until respon.length()) {
            val obj = respon.getJSONObject(i)
            Utils.news.add(TwitterModels.Companion.Tweet(obj.getString("created_at"),
                    obj.getString("text"),
                    obj.getJSONObject("user").getString("name"),"",
                    obj.getJSONObject("user").getString("profile_image_url_https")))
        }

        news.adapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userInterfaceInit()
        getTweets()
    }

    private fun userInterfaceInit(){
        news.layoutManager = LinearLayoutManager(this)
        news.adapter = NewsAdapter()
    }

    private fun getTweets(){
        if (getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).contains("token")) {
            GetTweets.create().getTweets("Bearer " + getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).getString("token", ""))
                    .enqueue(this)
        } else {
            TwitterAuth.create().authorize(Utils.createBase64AuthString()).enqueue(object : Callback<TwitterModels.Companion.TwitterAuth>{

                override fun onFailure(call: Call<TwitterModels.Companion.TwitterAuth>?, t: Throwable?) = t!!.printStackTrace()

                override fun onResponse(call: Call<TwitterModels.Companion.TwitterAuth>?, response: Response<TwitterModels.Companion.TwitterAuth>?) {
                    getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putString("token", response!!.body()!!.access_token).apply()
                    GetTweets.create().getTweets("Bearer " + response.body()!!.access_token).enqueue(this@MainActivity)
                }
            })
        }
    }
}
