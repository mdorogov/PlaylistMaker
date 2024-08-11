package com.practicum.playlistmaker.search.domain.api

import android.content.SharedPreferences
import com.practicum.playlistmaker.search.data.models.Track

interface SearchHistoryInteractor {
    fun addTrackToArray(track: Track)
    fun createTrackArrayListFromJson(): ArrayList<Track>
    fun cleanHistory()
    fun createTrackListToJson(tracks: ArrayList<Track>): String
    fun saveToHistory(jsonHistory: String)
    fun setSharedPrefListener(sharedListener: SharedPreferences.OnSharedPreferenceChangeListener)

}