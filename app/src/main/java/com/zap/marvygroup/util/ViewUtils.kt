package com.zap.marvygroup.util

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.google.android.material.snackbar.Snackbar
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import es.dmoral.toasty.Toasty
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun Context.toast(message: String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}
fun CircularProgressBar.show(){
    visibility = View.VISIBLE
}
fun CircularProgressBar.hide(){
    visibility = View.GONE
}
@RequiresApi(Build.VERSION_CODES.O)
fun View.snackbar(message: String){
    Snackbar.make(this,message,Snackbar.LENGTH_LONG).also {
        snackbar ->
        snackbar.setAction("OK"){
            snackbar.dismiss()
        }
    }.show()

}
fun showToast(context: Context, messageResId: String?, isSuccess: Boolean) {
    if (isSuccess)
        Toasty.success(context, messageResId!!, Toast.LENGTH_SHORT).show()
    else
        Toasty.error(context, messageResId!!, Toast.LENGTH_SHORT).show()
}