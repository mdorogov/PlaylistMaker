package com.practicum.playlistmaker.search.mapper

import java.text.SimpleDateFormat
import java.util.Locale

object MillisConverter {
    fun millisToMinutesAndSeconds(millis: String): String {
        val num = millis.toLong()
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(num)
    }
}