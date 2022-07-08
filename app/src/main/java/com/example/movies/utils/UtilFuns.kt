package com.example.movies.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
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

fun View.setOnFocused(action: () -> Unit) {
    setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            action()
        }
    }
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}


