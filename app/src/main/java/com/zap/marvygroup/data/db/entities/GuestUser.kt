package com.zap.marvygroup.data.db.entities

import java.io.File

data class GuestUser(
    var name:String? = null,
    var mobile: String? = null,
    var email:String? = null,
    var subject:String? = null,
    var message:String? = null,
    var resume: File? = null
)