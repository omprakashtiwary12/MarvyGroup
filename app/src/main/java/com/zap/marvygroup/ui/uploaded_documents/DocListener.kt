package com.zap.marvygroup.ui.uploaded_documents

interface DocListener {
    fun onStarted()
    fun onSuccess(docData: ArrayList<DocData>)
    fun onFailure(message:String)
}