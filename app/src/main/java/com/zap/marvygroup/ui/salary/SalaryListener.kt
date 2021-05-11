package com.zap.marvygroup.ui.salary

import com.zap.marvygroup.data.db.entities.MonthYear


interface SalaryListener {
    fun onStarted()
    fun onSuccess(monthYear: MonthYear)
    fun onFailure(message:String)
}