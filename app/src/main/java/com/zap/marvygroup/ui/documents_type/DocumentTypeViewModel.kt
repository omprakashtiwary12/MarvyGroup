package com.zap.marvygroup.ui.documents_type

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zap.marvygroup.data.repositories.UserRepository
import com.zap.marvygroup.util.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DocumentTypeViewModel (private val repository: UserRepository):ViewModel(){
    val quotes: LiveData<List<String>?> = MutableLiveData()
    private val context = Application()
    private val TAG = "DocumentTypeViewModel"
    fun getDocumentResponse(){
       Coroutines.main {
           val response = repository.getDocumentType()
           Log.e("response",response.toString())
       }
    }
    suspend fun getQuotes(): DocTypeResponse {
        return withContext(Dispatchers.IO) {
            repository.getDocumentType()
        }
    }
}