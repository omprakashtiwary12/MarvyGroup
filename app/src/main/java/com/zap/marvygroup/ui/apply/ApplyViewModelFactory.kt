package com.zap.marvygroup.ui.apply

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zap.marvygroup.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class ApplyViewModelFactory(
    private val repository: UserRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ApplyViewModel(repository) as T
    }
}