package com.example.movies.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

object DateManager {
    @SuppressLint("SimpleDateFormat")
    fun convertFromISO(dateString: String, format: String = "yyyy-MM-dd'T'hh:mm:ss.SSS'Z'"): String {
        val df = SimpleDateFormat(format)
        val date = df.parse(dateString) ?: return ""
        return SimpleDateFormat("yyyy-MM-dd").format(date).toString()
    }
}