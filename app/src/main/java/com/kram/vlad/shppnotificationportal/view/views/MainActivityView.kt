package com.kram.vlad.shppnotificationportal.view.views

import com.kram.vlad.shppnotificationportal.presenters.base.MVPresenter
import com.kram.vlad.shppnotificationportal.view.base.BaseView

/**
 * Created by vlad on 31.03.2018.
 * View for MainActivity
 */
interface MainActivityView {
    interface View: BaseView{
        override fun showMessage(resId: Int) {}
        override fun next() {}
        override fun close() {}

        fun notifyDataSetChanged()
    }
    interface Presenter: MVPresenter<View>{
        fun getTweets()
        fun getTweets(maxId: Long)
    }
}