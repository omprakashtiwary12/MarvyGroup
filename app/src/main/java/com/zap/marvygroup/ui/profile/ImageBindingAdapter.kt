package com.zap.marvygroup.ui.profile

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

object ImageBindingAdapter {
    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageUrl(view: ImageView, url: String?) {
        Glide.with(view.context).load(url).into(view)
    }
}