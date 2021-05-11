package com.zap.marvygroup.ui.profile_image

interface ProfileListener {
    fun onStarted()
    fun onSuccess(uploadFile: String)
    fun onFailure(message: String?)
}