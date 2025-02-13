package com.practicum.playlistmaker.library.mapper

object WordFormConverter {
    fun getTrackWordForm(sum: Int) : String {
        return when {
            sum % 100 in 11..19 -> "$sum треков"
            sum % 10 == 1 -> "$sum трек"
            sum % 10 in 2..4 -> "$sum трека"
            else -> "$sum треков"
        }
    }

    fun getDurationWordForm(min: Int): String {
        return when {
            min % 100 in 11..19 -> "$min минут"
            min % 10 == 1 -> "$min минута"
            min % 10 in 2..4 -> "$min минуты"
            else -> "$min минут"
        }
    }
}