package com.practicum.playlistmaker.search.domain.impl


import android.content.SharedPreferences
import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import java.util.concurrent.Executors

class TracksInteractorImpl(private val trackSearcher: TrackSearchInteractor,
                           private val searchHistoryRepository: SearchHistoryRepository) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            when (val resource = trackSearcher.searchTrack(expression)) {
                is Resource.Success -> {
                    consumer.consume(resource.data, null)
                }

                is Resource.Error -> {
                    consumer.consume(null, resource.message)
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