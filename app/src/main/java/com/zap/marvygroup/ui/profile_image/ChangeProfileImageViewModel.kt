package com.zap.marvygroup.ui.profile_image

import android.view.View
import androidx.lifecycle.ViewModel
import com.zap.marvygroup.data.repositories.UserRepository
import com.zap.marvygroup.util.ApiException
import com.zap.marvygroup.util.Coroutines
import com.zap.marvygroup.util.NoInternetException
import java.io.File
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

class ChangeProfileImageViewModel(
    val repository: UserRepository
):ViewModel(){
    var employee_code: String?=null
    var token:String?=null
    var type = "test"
    var profileListener: ProfileListener? = null
    var imageFile: File? = null


    fun uploadButtonClick(view: View){
        if (imageFile == null){
            profileListener?.onFailure("You must select a file to upload")
        }
        Coroutines.main {
            profileListener?.onStarted()
            try {
                val documentsResponse = repository.changeProfileImage(employee_code!!,token!!,imageFile!!)
                documentsResponse.uploaded_file?.let {
                    repository.saveUserImage(it)
                    profileListener?.onSuccess(it)
                    return@main
                }
                profileListener?.onFailure(documentsResponse.message)
            }catch (e: ApiException){
                profileListener?.onFailure(e.message)
            }catch (e: NoInternetException){
                profileListener?.onFailure(e.message)
            }catch (e: SocketTimeoutException){
                profileListener?.onFailure(e.message)
            }
        }
    }

}