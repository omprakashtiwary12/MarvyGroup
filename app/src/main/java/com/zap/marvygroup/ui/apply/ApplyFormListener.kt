package com.zap.marvygroup.ui.apply

import com.zap.marvygroup.data.db.entities.GuestUser

interface ApplyFormListener {
    fun onStarted()
    fun onSuccess(success:String)
    fun onFailure(message:String)
}