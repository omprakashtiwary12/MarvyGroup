package com.zap.marvygroup.ui.attendance

import com.zap.marvygroup.data.db.entities.Attendance

interface AttendanceOutListener {
    fun onStartedOut()
    fun onSuccessOut(address: Address)
    fun onFailureOut(message: String?)
}