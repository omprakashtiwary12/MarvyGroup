package com.zap.marvygroup.util

import com.zap.marvygroup.R

sealed class GpsStatus {
    data class Enabled(val message: Int = R.string.gps_status_enabled):GpsStatus()
    data class Disabled(val message: Int = R.string.gps_status_disabled):GpsStatus()
}