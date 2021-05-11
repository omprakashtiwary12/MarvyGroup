package com.zap.marvygroup.ui.documents_type


import com.google.gson.annotations.SerializedName

data class Record(
    @SerializedName("document_name")
    val documentName: String,
    @SerializedName("document_type")
    val documentType: String,
    @SerializedName("emp_code")
    val employeeCode: String,
    @SerializedName("update_date")
    val updateDate: String
)