package com.zap.marvygroup.ui.documents_type

interface DocListListener {
    fun onStarted()
    fun onSuccess(docData: List<Record>)
    fun onFailure(message:String)
}