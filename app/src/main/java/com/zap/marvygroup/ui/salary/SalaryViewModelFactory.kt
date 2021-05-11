package com.zap.marvygroup.ui.salary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zap.marvygroup.data.repositories.UserRepository


class SalaryViewModelFactory(
    private val repository: UserRepository
):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SalaryViewModel(repository)as T
    }

}