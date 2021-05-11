package com.zap.marvygroup.ui.contactus

import androidx.lifecycle.LiveData
import com.zap.marvygroup.data.db.entities.ContactUser


interface ContactListener {
    fun onStarted()
    fun onSuccess(user: ContactUser)
    fun onFailure(message:String)
}