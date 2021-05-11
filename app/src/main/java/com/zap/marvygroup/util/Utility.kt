package com.zap.marvygroup.util

import android.content.Context
import android.widget.Toast
import es.dmoral.toasty.Toasty

object Utility {
    fun showToast(context: Context, messageResId: String?, isSuccess: Boolean) {
        if (isSuccess)
            Toasty.success(context, messageResId!!, Toast.LENGTH_SHORT).show()
        else
            Toasty.error(context, messageResId!!, Toast.LENGTH_SHORT).show()
    }
}