package com.zap.marvygroup.ui.reset

import androidx.lifecycle.LiveData
import com.zap.marvygroup.data.db.entities.ResetUser


interface ResetListener {
    fun onStarted()
    fun onSuccess(user: ResetUser)
    fun onFailure(message:String)
}