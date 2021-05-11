package com.zap.marvygroup.ui.auth

import android.content.Intent
import android.view.View
import androidx.lifecycle.ViewModel
import com.zap.marvygroup.data.repositories.UserRepository
import com.zap.marvygroup.ui.apply.ApplyForm
import com.zap.marvygroup.ui.reset.ForgotPassword
import com.zap.marvygroup.ui.splash.LocationViewModel
import com.zap.marvygroup.util.ApiException
import com.zap.marvygroup.util.Coroutines
import com.zap.marvygroup.util.NoInternetException
import java.io.File
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException


class AuthViewModel(
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
    var photo: String? = null
    var locationViewModel: LocationViewModel?= null
    val PDF : Int = 0
    var authListener:AuthListener?=null
    fun getLoggedInUser() = repository.getUser()

    fun onLoginButtonClick(view: View) {
        authListener?.onStarted()
        if (username.isNullOrEmpty() || password.isNullOrEmpty()) {
          authListener?.onFailure("Please enter your employee code and password ")
         return
        }

        Coroutines.main {
            try {
                val authResponse = repository.userLogin(username!!,password!!)
                authResponse.user?.let {
                    authListener?.onSuccess(it)
                    repository.saveUser(it)
                    it.token?.let { it1 -> repository.saveToken(it1) }
                    it.name?.let { it1 -> repository.saveUserName(it1) }
                    it.photo?.let { it1 -> repository.saveUserImage(it1) }
                    it.employee_code?.let { it1 -> repository.saveEcode(it1) }
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

    fun onApplyButtonClicked(view: View){
        Intent(view.context, ApplyForm::class.java).also {
            view.context.startActivity(it)
        }

    }
    fun onForgotButtonClicked(view: View){
        Intent(view.context, ForgotPassword::class.java).also {
            view.context.startActivity(it)
        }

    }

    fun onLogin(view: View){
        Intent(view.context,LoginActivity::class.java).also {
            view.context.startActivity(it)
        }
    }
}