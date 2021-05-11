package com.zap.marvygroup.ui.auth

import androidx.lifecycle.LiveData
import com.zap.marvygroup.data.db.entities.User


interface AuthListener {
    fun onStarted()
    fun onSuccess(user: User)
    fun onFailure(message:String)
}