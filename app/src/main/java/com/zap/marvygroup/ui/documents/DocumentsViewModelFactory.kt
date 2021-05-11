package com.zap.marvygroup.ui.documents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zap.marvygroup.data.repositories.UserRepository


@Suppress("UNCHECKED_CAST")
class DocumentsViewModelFactory(
    private val repository: UserRepository
):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DocumentsViewModel(repository) as T
    }

}