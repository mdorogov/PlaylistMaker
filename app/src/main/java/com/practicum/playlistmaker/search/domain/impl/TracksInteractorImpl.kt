package com.practicum.playlistmaker.search.domain.impl

import android.content.SharedPreferences
import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import java.util.concurrent.Executors
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TracksInteractorImpl(private val trackSearcher: TrackSearchInteractor,
                           private val searchHistoryRepository: SearchHistoryRepository) : TracksInteractor {

    override fun searchTracks(expression: String): Flow<Pair<List<Track>?, String?>> {
        return trackSearcher.searchTrack(expression).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data, null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }


    override fun cleanHistory() {
        searchHistoryRepository.cleanHistory()
    }

    override fun createTrackArrayListFromJson(): ArrayList<Track> {
        return searchHistoryRepository.createTrackArrayListFromJson()
    }

    override fun setSharedPrefListener(sharedListener: SharedPreferences.OnSharedPreferenceChangeListener) {
        searchHistoryRepository.setSharedPrefListener(sharedListener)
    }

    override fun addTrackToArray(track: Track) {
        searchHistoryRepository.addTrackToArray(track)
    }
}