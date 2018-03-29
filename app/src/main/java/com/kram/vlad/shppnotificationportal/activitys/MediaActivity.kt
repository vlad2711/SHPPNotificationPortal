package com.kram.vlad.shppnotificationportal.activitys

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.afollestad.easyvideoplayer.EasyVideoCallback
import com.afollestad.easyvideoplayer.EasyVideoPlayer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kram.vlad.shppnotificationportal.R
import com.kram.vlad.shppnotificationportal.utils.Utils
import kotlinx.android.synthetic.main.activity_media.*
import java.lang.Exception

class MediaActivity : AppCompatActivity(), EasyVideoCallback {
    override fun onRetry(player: EasyVideoPlayer?, source: Uri?) {}
    override fun onPrepared(player: EasyVideoPlayer?) {}
    override fun onStarted(player: EasyVideoPlayer?) {}
    override fun onCompletion(player: EasyVideoPlayer?) {}
    override fun onSubmit(player: EasyVideoPlayer?, source: Uri?) {}
    override fun onBuffering(percent: Int) {}
    override fun onPreparing(player: EasyVideoPlayer?) {}
    override fun onError(player: EasyVideoPlayer?, e: Exception?) {}
    override fun onPaused(player: EasyVideoPlayer?) {}

    var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media)
        userInterfaceInit()
    }

    private fun userInterfaceInit(){
        position = intent.extras.getInt("position")
        if (Utils.news[position].mediaUrls[0].type == "photo") {
            image.setImageUrl(Utils.news[position].mediaUrls[0].url)
            image.visibility = View.VISIBLE
        } else{
            video.setSource(Uri.parse(Utils.news[position].mediaUrls[0].url))
            video.visibility = View.VISIBLE
            video.setCallback(this)
            if(Utils.news[position].mediaUrls[0].type != "video"){
                video.setLoop(true)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        video.pause()
    }

    private fun ImageView.setImageUrl(url: String) = Glide.with(this).load(Uri.parse(url))
            .apply(RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(Target.SIZE_ORIGINAL)).into(this)

}
