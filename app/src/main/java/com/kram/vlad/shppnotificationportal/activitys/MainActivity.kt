package com.kram.vlad.shppnotificationportal.activitys

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.kram.vlad.shppnotificationportal.R
import com.kram.vlad.shppnotificationportal.utils.Utils
import com.kram.vlad.shppnotificationportal.adapters.NewsAdapter
import com.kram.vlad.shppnotificationportal.presenters.MainActivityPresenter
import com.kram.vlad.shppnotificationportal.view.base.BaseActivity
import com.kram.vlad.shppnotificationportal.view.views.MainActivityView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), MainActivityView.View {

    private val TAG = this::class.java.simpleName
    private val presenter = MainActivityPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userInterfaceInit()

        presenter.attachView(this)
        presenter.viewIsReady()
        presenter.getTweets()

        if(savedInstanceState != null) news.scrollToPosition(savedInstanceState.getInt("position"))
    }

    private fun userInterfaceInit() {
        news.layoutManager = LinearLayoutManager(this)
        val adapter = NewsAdapter()
        adapter.setHasStableIds(true)

        news.adapter = adapter
        Utils.news = ArrayList()
    }

    fun getTweetsMaxId(maxId: Long) = presenter.getTweets(maxId)


    override fun showMessage(resId: Int) {
        super.showMessage(resId)
        Toast.makeText(this, resId, Toast.LENGTH_LONG).show()
    }

    override fun next() {
        super.next()
        startActivity(Intent(this, MediaActivity::class.java))
    }

    override fun close() {
        super.close()
        finish()
    }

    override fun notifyDataSetChanged() {
        if(!news.isComputingLayout) news.adapter.notifyDataSetChanged()
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.putInt("position", (news.adapter as NewsAdapter).position)
    }
}
