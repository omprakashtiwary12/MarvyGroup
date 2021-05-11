package com.zap.marvygroup.ui.documents

import android.view.View
import androidx.lifecycle.ViewModel
import com.zap.marvygroup.data.repositories.UserRepository
import com.zap.marvygroup.util.ApiException
import com.zap.marvygroup.util.Coroutines
import com.zap.marvygroup.util.NoInternetException
import java.io.File
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException

class DocumentsViewModel (
    private val repository: UserRepository

):ViewModel(){
    var employee_code: String?=null
    var token:String?=null
    var type :String?= null
    var documentListener: DocumentListener? = null

    var myFile: File? = null

    fun uploadButtonClick(view: View) {
        if (myFile == null){
            documentListener?.onFailure("You must select a file to upload")
        }

        Coroutines.main {
            documentListener?.onStarted()
            try {
             //  val documentsResponse = repository.attachDocFile(employee_code!!,token!!,type!!,myFile!!)
//                   documentsResponse.uploaded_file?.let {
//                       documentListener?.onSuccess(it)
//                       return@main
//                   }
              //  documentListener?.onFailure(documentsResponse.message)
            }catch (e: ApiException){
                documentListener?.onFailure(e.message)
            }catch (e: NoInternetException){
                documentListener?.onFailure(e.message)
            }catch (e:SocketTimeoutException){
                documentListener?.onFailure(e.message)
            }
        }
    }
}