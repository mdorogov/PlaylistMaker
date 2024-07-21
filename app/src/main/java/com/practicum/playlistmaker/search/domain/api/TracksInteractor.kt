package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.data.SearchHistory
import com.practicum.playlistmaker.search.data.models.Track

interface TracksInteractor {
   fun searchTracks(expression: String, consumer: TracksConsumer)

    fun getSearchHistory(): SearchHistory

    interface TracksConsumer{
        fun consume(foundTracks: List<Track>?, errorMessage: String?)
    }
}