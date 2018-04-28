package com.kram.vlad.shppnotificationportal.presenters

import android.content.Context
import android.text.Html
import android.util.Log
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
import java.util.*
import kotlin.collections.ArrayList

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

            var tweetUrl = ""
            if (obj.has("extended_entities")) {
                val media = obj.getJSONObject("extended_entities").getJSONArray("media")
                for (j in 0 until media.length()) {
                    tweetUrl = media.getJSONObject(j).getString("url")
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

            Log.d(TAG, obj.toString(4))
            var text = obj.getString("full_text");

            val urls = ArrayList<TwitterModels.Companion.TwitterUrl>()
            if(obj.getJSONObject("entities").has("urls")) {
                val urlsJson = obj.getJSONObject("entities").getJSONArray("urls")

                for (j in 0 until urlsJson.length()) {
                    val url = urlsJson.getJSONObject(j)
                    val indices = ArrayList<Int>()
                    indices.add(url.getJSONArray("indices").getInt(0))
                    indices.add(url.getJSONArray("indices").getInt(1))

                    urls.add(TwitterModels.Companion.TwitterUrl(url.getString("url"),
                            url.getString("expanded_url"),
                            url.getString("display_url"),
                            indices))


                }
            }


            if(!Objects.equals(tweetUrl, "")){
                text = text.replace(tweetUrl, "")
            }

            Utils.news.add(TwitterModels.Companion.Tweet(
                    obj.getLong("id"),
                    tweetUrl,
                    obj.getString("created_at"),
                    text,
                    obj.getJSONObject("user").getString("name"),
                    obj.getJSONObject("user").getString("profile_image_url_https"), medias, urls))

            sqLiteHelper.addTweet(Utils.news[Utils.news.size - 1])
        }

        Utils. newsBufSize = Utils.news.size
        view!!.notifyDataSetChanged()
    }
}