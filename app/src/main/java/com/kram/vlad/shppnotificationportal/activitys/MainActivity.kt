package com.kram.vlad.shppnotificationportal.activitys

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.kram.vlad.shppnotificationportal.R
import com.kram.vlad.shppnotificationportal.utils.Utils
import com.kram.vlad.shppnotificationportal.adapters.NewsAdapter
import com.kram.vlad.shppnotificationportal.rest.pojo.TwitterModels
import com.kram.vlad.shppnotificationportal.utils.RESTUtils
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), Callback<ResponseBody> {
    private val TAG = this::class.java.simpleName
    private val restUtils = RESTUtils(this)
    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) = t!!.printStackTrace()

    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
        val json = response!!.body()!!.string()
        val respon = JSONArray(json)
        for (i in 0 until respon.length()) {
            val obj = respon.getJSONObject(i)
            val medias = ArrayList<TwitterModels.Companion.Media>()

            if (obj.has("extended_entities")) {
                val media = obj.getJSONObject("extended_entities").getJSONArray("media")
                for (j in 0 until media.length()) {
                    medias.add(TwitterModels.Companion.Media("", "", ""))
                    medias[j].type = media.getJSONObject(j).getString("type")
                    if(medias[j].type == "photo") {
                        medias[j].url = media.getJSONObject(j).getString("media_url")
                        val sizes = media.getJSONObject(j).getJSONObject("sizes")
                        medias[j].sizes.add(TwitterModels.Companion.ImageSizes(sizes.getJSONObject("large").getInt("w"), sizes.getJSONObject("large").getInt("h")))
                        medias[j].sizes.add(TwitterModels.Companion.ImageSizes(sizes.getJSONObject("medium").getInt("w"), sizes.getJSONObject("medium").getInt("h")))
                        medias[j].sizes.add(TwitterModels.Companion.ImageSizes(sizes.getJSONObject("small").getInt("w"), sizes.getJSONObject("small").getInt("h")))
                        medias[j].sizes.add(TwitterModels.Companion.ImageSizes(sizes.getJSONObject("thumb").getInt("w"), sizes.getJSONObject("thumb").getInt("h")))
                    } else{
                        medias[j].previewUrl = media.getJSONObject(j).getString("media_url")
                        medias[j].url = media.getJSONObject(j).getJSONObject("video_info").getJSONArray("variants").getJSONObject(0).getString("url")
                    }
                }
            }

            Log.d(TAG, obj.toString(0))

            Utils.news.add(TwitterModels.Companion.Tweet(
                    obj.getLong("id"),
                    obj.getString("created_at"),
                    obj.getString("text"),
                    obj.getJSONObject("user").getString("name"),
                    obj.getJSONObject("user").getString("profile_image_url_https"), medias))

        }

        news.adapter.notifyDataSetChanged()
        Utils.newsBufSize = Utils.news.size
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userInterfaceInit()
        restUtils.getTweets(this)
    }

    private fun userInterfaceInit() {
        news.layoutManager = LinearLayoutManager(this)
        val adapter = NewsAdapter()
        adapter.setHasStableIds(true)

        news.adapter = adapter
        Utils.news = ArrayList()
    }

    fun getTweetsMaxId(maxId: Long) = restUtils.getTweetsMaxId(maxId, this)
}
