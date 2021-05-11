package com.zap.marvygroup.ui.documents_type

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zap.marvygroup.data.repositories.UserRepository

class DocumentTypeFactory (
    private val repository: UserRepository
):ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DocumentTypeViewModel(repository) as T
    }
}