package com.zap.marvygroup.util

import android.app.Application
import androidx.lifecycle.AndroidViewModel

public class SharedImageViewModel (application: Application) : AndroidViewModel(application){
    var photo:String? = null
}