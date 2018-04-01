package com.kram.vlad.shppnotificationportal.presenters

import android.content.Context
import com.kram.vlad.shppnotificationportal.model.SQLiteHelper
import com.kram.vlad.shppnotificationportal.presenters.base.BasePresenter
import com.kram.vlad.shppnotificationportal.rest.pojo.TwitterModels
import com.kram.vlad.shppnotificationportal.utils.NetworkUtils
import com.kram.vlad.shppnotificationportal.utils.RESTUtils
import com.kram.vlad.shppnotificationportal.utils.Utils
import com.kram.vlad.shppnotificationportal.view.views.MainActivityView
import okhttp3.ResponseBody
import org.json.JSONArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by vlad on 31.03.2018.
 * Presenter for MainActivity
 */
class MainActivityPresenter(val context: Context) : BasePresenter<MainActivityView.View>(), MainActivityView.Presenter, Callback<ResponseBody> {
    private val TAG = this::class.java.simpleName
    private val restUtils = RESTUtils(context)

    override fun getTweets(maxId: Long) {
        if (NetworkUtils().isOnline(context)) {
            restUtils.getTweetsMaxId(maxId, this)
        } else {
            SQLiteHelper(context).getTweet(maxId)
            view!!.notifyDataSetChanged()
        }
    }

    override fun getTweets() {
        if (NetworkUtils().isOnline(context)) {
            restUtils.getTweets(this)
        } else {
            SQLiteHelper(context).getTweet()
            view!!.notifyDataSetChanged()
        }
    }

    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) = t!!.printStackTrace()

    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
        val json = response!!.body()!!.string()
        val respon = JSONArray(json)
        val sqLiteHelper = SQLiteHelper(context)

        for (i in 0 until respon.length()) {
            val obj = respon.getJSONObject(i)
            val medias = ArrayList<TwitterModels.Companion.Media>()

            if (obj.has("extended_entities")) {
                val media = obj.getJSONObject("extended_entities").getJSONArray("media")
                for (j in 0 until media.length()) {
                    medias.add(TwitterModels.Companion.Media("", "", ""))
                    medias[j].type = media.getJSONObject(j).getString("type")
                    if (medias[j].type == "photo") {
                        medias[j].url = media.getJSONObject(j).getString("media_url")
                        val sizes = media.getJSONObject(j).getJSONObject("sizes")
                        medias[j].sizes.add(TwitterModels.Companion.ImageSizes(sizes.getJSONObject("large").getInt("w"), sizes.getJSONObject("large").getInt("h")))
                        medias[j].sizes.add(TwitterModels.Companion.ImageSizes(sizes.getJSONObject("medium").getInt("w"), sizes.getJSONObject("medium").getInt("h")))
                        medias[j].sizes.add(TwitterModels.Companion.ImageSizes(sizes.getJSONObject("small").getInt("w"), sizes.getJSONObject("small").getInt("h")))
                        medias[j].sizes.add(TwitterModels.Companion.ImageSizes(sizes.getJSONObject("thumb").getInt("w"), sizes.getJSONObject("thumb").getInt("h")))
                    } else {
                        medias[j].previewUrl = media.getJSONObject(j).getString("media_url")
                        medias[j].url = media.getJSONObject(j).getJSONObject("video_info").getJSONArray("variants").getJSONObject(0).getString("url")
                    }
                }
            }

            // Log.d(TAG, obj.toString(0))

            Utils.news.add(TwitterModels.Companion.Tweet(
                    obj.getLong("id"),
                    obj.getString("created_at"),
                    obj.getString("text"),
                    obj.getJSONObject("user").getString("name"),
                    obj.getJSONObject("user").getString("profile_image_url_https"), medias))

            sqLiteHelper.addTweet(Utils.news[i])
        }

        Utils.newsBufSize = Utils.news.size
        view!!.notifyDataSetChanged()


    }
}