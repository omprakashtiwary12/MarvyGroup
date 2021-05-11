package com.zap.marvygroup.util

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.zap.marvygroup.ui.splash.LocationLiveData

public class SharedLocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationData = LocationLiveData(application)
    var latValue:Double = 0.0
    var longValue:Double = 0.0
    var photo:String? = null
    fun getLocationData() = locationData
}
