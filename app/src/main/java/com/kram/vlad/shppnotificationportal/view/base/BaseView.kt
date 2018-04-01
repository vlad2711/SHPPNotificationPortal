package com.kram.vlad.shppnotificationportal.view.base

/**
 * Created by vlad on 31.03.2018.
 * Base MVP view
 */
interface BaseView {
    fun showMessage(resId: Int)
    fun next()
    fun close()
}