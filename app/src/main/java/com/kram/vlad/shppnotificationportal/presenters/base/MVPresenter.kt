package com.kram.vlad.shppnotificationportal.presenters.base

import com.kram.vlad.shppnotificationportal.view.base.BaseView


/**
 * Created by vlad on 31.03.2018.
 * Base presenter for MVP
 */
interface MVPresenter<in T: BaseView> {
    fun attachView(mvpView: T)
    fun viewIsReady()
    fun detachView()
    fun destroy()
}