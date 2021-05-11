package com.zap.marvygroup.ui.uploaded_pdf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zap.marvygroup.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class PdfViewModelFactory (
    private val repository: UserRepository
): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UploadedPdfViewModel(repository) as T
    }

}