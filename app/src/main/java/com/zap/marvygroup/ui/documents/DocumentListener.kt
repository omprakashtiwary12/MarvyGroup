package com.zap.marvygroup.ui.documents

interface DocumentListener {
    fun onStarted()
    fun onSuccess(uploadFile: String)
    fun onFailure(message: String?)
}