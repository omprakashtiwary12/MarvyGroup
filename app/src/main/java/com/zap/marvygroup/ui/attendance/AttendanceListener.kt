package com.zap.marvygroup.ui.attendance

import com.zap.marvygroup.data.db.entities.Attendance

interface AttendanceListener {
    fun onStarted()
    fun onSuccess(address: Address)
    fun onFailure(message: String?)
}