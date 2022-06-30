package com.example.movies.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T> launchOn(call: suspend () -> T): DataResult<T> {
    return try {
        val result = call()
        DataResult.Success(result)
    } catch (ex: Exception) {
        DataResult.Failed(ex)
    }
}

fun <T> createListedLiveData(): MutableLiveData<List<T>> {
    return MutableLiveData()
}
fun <T> createLiveData(): MutableLiveData<T> {
    return MutableLiveData()
}

fun <T> MutableLiveData<T>.toLiveData(): LiveData<T> {
    return this
}

suspend fun <T> MutableLiveData<T>.assignValue(value: T) {
    withContext(Dispatchers.Main) {
        this@assignValue.value = value
    }
}