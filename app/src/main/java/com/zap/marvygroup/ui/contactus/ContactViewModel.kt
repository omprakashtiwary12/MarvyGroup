package com.zap.marvygroup.ui.contactus

import android.view.View
import androidx.lifecycle.ViewModel
import com.zap.marvygroup.data.repositories.UserRepository
import com.zap.marvygroup.util.ApiException
import com.zap.marvygroup.util.Coroutines
import com.zap.marvygroup.util.NoInternetException

import java.net.SocketTimeoutException

class ContactViewModel(
    private val repository: UserRepository
): ViewModel() {

    var name:String? = null
    var email:String? = null
    var subject:String? = null
    var message:String? = null
    var contactListener: ContactListener?=null

    fun onApplyButtonClick(view: View){
     contactListener?.onStarted()

        if (name.isNullOrEmpty()){
            contactListener?.onFailure("Name is required")
            return
        }
        if (email.isNullOrEmpty()){
            contactListener?.onFailure("Email is required")
            return
        }
        if (subject.isNullOrEmpty()){
            contactListener?.onFailure("Subject is required")
            return
        }
        if (message.isNullOrEmpty()){
            contactListener?.onFailure("Message is required")
            return
        }
        Coroutines.main {
          try {
              val contactFormResponse = repository.addContactInfo(name!!,email!!,subject!!,message!!)
                   contactFormResponse.contactUser?.let {
                       contactListener?.onSuccess(it)
                       return@main
                   }
              contactListener?.onFailure(contactFormResponse.message!!)

          }catch (e: ApiException){
              contactListener?.onFailure(e.message!!)
          }catch (e: NoInternetException){
              contactListener?.onFailure(e.message!!)
          }catch (e: SocketTimeoutException){
              contactListener?.onFailure(e.message!!)
          }
        }

    }

}
