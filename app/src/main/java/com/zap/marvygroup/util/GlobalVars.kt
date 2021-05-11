package com.zap.marvygroup.util

import android.app.Application


class GlobalVars: Application() {

    private var isConnected: Boolean = false
    private var imageString: String? =null

    fun getImage(): String {
        return imageString.toString()
    }

    fun setImage(imageString: String) {
        this.imageString = imageString
    }
}