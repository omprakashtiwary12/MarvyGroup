package com.zap.marvygroup.ui.documents_type

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.zap.marvygroup.data.repositories.UserRepository
import com.zap.marvygroup.ui.documents.DocumentListener
import com.zap.marvygroup.util.ApiException
import com.zap.marvygroup.util.Coroutines
import com.zap.marvygroup.util.NoInternetException
import java.io.File
import java.net.SocketTimeoutException

class DocViewModel (
    private val repository: UserRepository
): ViewModel(){
    var employee_code: String?=null
    var token:String?=null
    var type :String?= null
    var documentListener: DocumentListener? = null

    var myFile: File? = null

//    fun uploadButtonClick(view: View) {
//        documentListener?.onStarted()
//        if (myFile == null){
//            documentListener?.onFailure("You must select a file to upload")
//            return
//        }
//        if(myFile!!.length() == 0L){
//            documentListener?.onFailure("File is empty")
//            return
//        }
//
//        Coroutines.main {
//
//            try {
//                val documentsResponse = repository.attachDocFile(employee_code!!,token!!,type!!,myFile!!)
//                documentsResponse.uploaded_file?.let {
//                    documentListener?.onSuccess(it)
//                    return@main
//                }
//                documentListener?.onFailure(documentsResponse.message)
//            }catch (e: ApiException){
//                documentListener?.onFailure("You are already logged in to another device, please logout")
//            }catch (e: NoInternetException){
//                documentListener?.onFailure(e.message)
//            }catch (e: SocketTimeoutException){
//                documentListener?.onFailure(e.message)
//            }
//        }
//    }



        suspend fun getDocList(){
            Coroutines.main {
                val response = repository.showDocListApi(employee_code!!)
                Log.e("responseLog",response.toString())
//                response.records?.let {
//                   Log.e("list",it.toString())
//                    return@main
//                }

            }
        }
}