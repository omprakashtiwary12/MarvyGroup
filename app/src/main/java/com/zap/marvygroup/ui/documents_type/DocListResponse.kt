package com.zap.marvygroup.ui.documents_type


import com.google.gson.annotations.SerializedName

data class DocListResponse(
    val isSuccessful: Boolean,
    val message: String,
    @SerializedName("Record")
    val record: List<Record>
)