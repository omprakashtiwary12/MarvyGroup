package com.zap.marvygroup.ui.uploaded_documents

data class DocResponse(
    val DocListData: ArrayList<DocData>,
    val isSuccessful: Boolean,
    val message: String
)