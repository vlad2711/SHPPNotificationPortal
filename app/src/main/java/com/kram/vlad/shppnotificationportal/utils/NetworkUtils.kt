package com.kram.vlad.shppnotificationportal.utils

import android.content.Context
import android.net.ConnectivityManager

/**
 * Created by vlad on 29.03.2018.
 * Utils for network
 */
class NetworkUtils() {
    fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return netInfo != null && netInfo.isConnectedOrConnecting
    }
}