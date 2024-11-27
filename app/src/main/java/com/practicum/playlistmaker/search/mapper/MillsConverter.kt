package com.practicum.playlistmaker.search.mapper

import java.text.SimpleDateFormat
import java.util.Locale

object MillisConverter {
    fun millisToMinutesAndSeconds(millis: String): String {
        val num = millis.toLong()
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(num)
    }

    fun millisToMinutes(millis: String): String {
        val num = millis.toLong()
        return SimpleDateFormat("mm", Locale.getDefault()).format(num)
    }

    fun millisToMinutes(millis: Long): Int {
        return SimpleDateFormat("m", Locale.getDefault()).format(millis).toInt()
    }
}