package com.zap.marvygroup.ui.uploaded_pdf

import androidx.lifecycle.ViewModel
import com.zap.marvygroup.data.repositories.UserRepository
import com.zap.marvygroup.ui.uploaded_documents.DocListener
import com.zap.marvygroup.util.Coroutines


class UploadedPdfViewModel(
    private val repository: UserRepository
):ViewModel(){
    var employee_code: String?=null
    var docListener: DocListener?= null
    suspend fun getPdfList(){
        Coroutines.main {
            docListener?.onStarted()
            val response = repository.showPdfUploadDoc(employee_code!!)

            response.DocListData.let {
                docListener?.onSuccess(it)
            }
        }
    }
}