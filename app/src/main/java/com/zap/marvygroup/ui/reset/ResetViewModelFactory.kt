package com.zap.marvygroup.ui.reset

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zap.marvygroup.data.repositories.UserRepository


class ResetViewModelFactory(
    private val repository: UserRepository
):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForgetViewModel(repository)as T
    }

}