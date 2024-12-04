package com.practicum.playlistmaker.search.domain.api

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.practicum.playlistmaker.search.data.models.Track

interface SearchHistoryRepository {
    fun addTrackToArray(track: Track)
    fun createTrackArrayListFromJson(): ArrayList<Track>
    fun cleanHistory()
    fun createTrackListToJson(tracks: ArrayList<Track>): String
    fun saveToHistory(jsonHistory: String)
    fun getChangedHistoryLiveData(): LiveData<Boolean>
}