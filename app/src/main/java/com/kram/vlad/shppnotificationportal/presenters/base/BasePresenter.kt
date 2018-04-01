package com.kram.vlad.shppnotificationportal.presenters.base

import com.kram.vlad.shppnotificationportal.view.base.BaseView

/**
 * Created by vlad on 31.03.2018.
 * Base presenter for views presenters
 */
public open class BasePresenter<T: BaseView> : MVPresenter<T> {
    var view: T? = null
        private set

    protected val isViewAttached: Boolean
        get() = view != null

    override fun attachView(mvpView: T) {view = mvpView}
    override fun viewIsReady() {}
    override fun detachView() {view = null}
    override fun destroy() {}
}