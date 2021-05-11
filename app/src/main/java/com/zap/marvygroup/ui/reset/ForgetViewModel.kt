package com.zap.marvygroup.ui.reset

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.zap.marvygroup.data.repositories.UserRepository
import com.zap.marvygroup.ui.auth.AuthListener
import com.zap.marvygroup.util.ApiException
import com.zap.marvygroup.util.Coroutines
import com.zap.marvygroup.util.NoInternetException
import java.io.File
import java.net.SocketTimeoutException


class ForgetViewModel(
   private val repository: UserRepository
) : ViewModel() {
    var username: String? = null
    var password: String? = null
    var name: String? = null
    var mobile: String? = null
    var email: String? = null
    var subject: String? = null
    var message: String? = null
    var resume: File? = null
    val PDF : Int = 0
    var authListener: AuthListener?=null
    fun getLoggedInUser() = repository.getUser()




    fun onResetButtonClick(view: View){
        authListener?.onStarted()
        if (username.isNullOrEmpty()){
            authListener?.onFailure("Plase enter your employee code ")
            return
        }
        Coroutines.main {
            try {
                val authResponse = repository.resetPassword(username!!)
                authResponse.user?.let {
                    authListener?.onSuccess(it)
                    return@main
                }
                authListener?.onFailure(authResponse.message!!)

            }catch (e: ApiException){
                authListener?.onFailure(e.message!!)

            }catch (e: NoInternetException){
                authListener?.onFailure(e.message!!)
            }catch (e:SocketTimeoutException){
                authListener?.onFailure(e.message!!)
            }
        }
    }

}