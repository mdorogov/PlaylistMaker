package com.practicum.playlistmaker.search.domain.api

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.practicum.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>>
    fun cleanHistory()
    fun createTrackArrayListFromJson(): ArrayList<Track>
    fun addTrackToArray(track: Track)
    fun getChangedHistoryLiveData(): LiveData<Boolean>

    interface TracksConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }

}