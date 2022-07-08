package com.example.movies.utils

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

object DateManager {
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.O)
    fun convertFromISO(dateString: String): String {
        val instant = Instant.parse(dateString)
        val date = Date(instant.epochSecond * 1000)
        return SimpleDateFormat("yyyy-MM-dd").format(date).toString()
    }
}