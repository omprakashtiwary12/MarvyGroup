package com.zap.marvygroup.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File

const val CURRENT_USER_ID = 0
@Entity
data class User(
    var id: Int? = null,
    var name: String? = null,
    var employee_code:String?=null,
    var email: String? = null,
    var token: String? = null,
    var photo: String? = null
){
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}
