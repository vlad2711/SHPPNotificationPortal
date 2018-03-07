package com.kram.vlad.shppnotificationportal.activitys

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kram.vlad.shppnotificationportal.Constants
import com.kram.vlad.shppnotificationportal.R
import com.kram.vlad.shppnotificationportal.Utils
import com.kram.vlad.shppnotificationportal.rest.pojo.TwitterModels
import com.kram.vlad.shppnotificationportal.rest.twitter.TwitterAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), Callback<TwitterModels.Companion.TwitterAuth> {

    private val TAG = this::class.java.simpleName

    override fun onFailure(call: Call<TwitterModels.Companion.TwitterAuth>?, t: Throwable?) {
        t!!.printStackTrace()
    }

    override fun onResponse(call: Call<TwitterModels.Companion.TwitterAuth>?, response: Response<TwitterModels.Companion.TwitterAuth>?) {
        getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putString("token", response!!.body()!!.access_token).apply()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, Utils.createBase64AuthString())
        if(!getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE).contains("token")) TwitterAuth.create().authorize("Basic " + Utils.createBase64AuthString()).enqueue(this)
    }
}
