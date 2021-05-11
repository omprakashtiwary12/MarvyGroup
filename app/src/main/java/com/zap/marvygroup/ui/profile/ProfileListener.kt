package com.zap.marvygroup.ui.profile

import com.zap.marvygroup.data.db.entities.Profile


interface ProfileListener {
    fun onStarted()
    fun onSuccess(profile: Profile)
    fun onFailure(message:String)
}