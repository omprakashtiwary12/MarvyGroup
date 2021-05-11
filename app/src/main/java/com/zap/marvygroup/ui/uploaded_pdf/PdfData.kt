package com.zap.marvygroup.ui.uploaded_pdf

import com.zap.marvygroup.ui.uploaded_documents.DocData


data class PdfData(
    val DocListData: List<DocData>,
    val isSuccessful: Boolean,
    val message: String
)