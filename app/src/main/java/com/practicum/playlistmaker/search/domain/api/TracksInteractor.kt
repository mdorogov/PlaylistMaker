package com.practicum.playlistmaker.search.domain.api

import android.content.SharedPreferences
import com.practicum.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
   fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>

    //new
    fun setSharedPrefListener(sharedListener: SharedPreferences.OnSharedPreferenceChangeListener)
    fun cleanHistory()
    fun createTrackArrayListFromJson(): ArrayList<Track>
    fun addTrackToArray(track: Track)

    interface TracksConsumer{
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}