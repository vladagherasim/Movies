package com.example.movies.utils

import androidx.lifecycle.MutableLiveData

sealed class DataResult<out T> {
    data class Success<out T>(val data: T) : DataResult<T>()
    data class Failed(val exception: Exception) : DataResult<Nothing>()

    suspend fun subscribeOver(
        exceptionsSubscriber: MutableLiveData<Exception>,
        onSuccess: suspend T.() -> Unit
    ) {
        when (this) {
            is Failed -> exceptionsSubscriber.assignValue(exception)
            is Success -> onSuccess(data)
        }
    }
}