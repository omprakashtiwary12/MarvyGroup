package com.zap.marvygroup.ui.attendance


import com.google.gson.annotations.SerializedName

data class UserAddress(
    val address: Address,
    val isSuccessful: Boolean,
    val message: String
)