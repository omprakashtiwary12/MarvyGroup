package com.my_taxi.monitorinternet

internal interface TaskFinished<T> {
    fun onTaskFinished(data: T)
}
