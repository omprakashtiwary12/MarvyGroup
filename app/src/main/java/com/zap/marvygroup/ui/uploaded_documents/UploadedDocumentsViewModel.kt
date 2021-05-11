package com.zap.marvygroup.ui.uploaded_documents

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.zap.marvygroup.data.repositories.UserRepository
import com.zap.marvygroup.util.Coroutines


class UploadedDocumentsViewModel(
    private val repository: UserRepository
):ViewModel(){
    var employee_code: String?=null
    var docListener:DocListener?= null
    suspend fun getDocList(){
        Coroutines.main {
            docListener?.onStarted()
            val response = repository.addUploadDoc(employee_code!!)

            response.DocListData.let {
                docListener?.onSuccess(it)
            }
        }
    }
}