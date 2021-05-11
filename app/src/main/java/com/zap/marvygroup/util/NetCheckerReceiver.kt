package com.zap.marvygroup.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

class NetCheckerReceiver : BroadcastReceiver() {

    interface NetCheckerReceiverListener {
        fun onNetworkConnectionChanged(isConnected: Boolean)
    }

    companion object {
        var netConnectionCheckerReceiver: NetCheckerReceiverListener? = null
    }

    override fun onReceive(context: Context, intent: Intent) {

        if (netConnectionCheckerReceiver != null) {
            netConnectionCheckerReceiver!!.onNetworkConnectionChanged(
                isConnectedOrConnecting(
                    context
                )
            )
        }
    }

    private fun isConnectedOrConnecting(context: Context): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connMgr.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting
    }
}