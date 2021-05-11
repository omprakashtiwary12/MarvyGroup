package com.zap.marvygroup.data.db.entities

data class Attendance(
    var latitude:Double? = null,
    var longitude:Double? = null,
    var is_entry:Boolean? = null,
    var employee_code:String? = null,
    var access_token:String? = null
)