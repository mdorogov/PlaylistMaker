package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.data.SearchHistory
import com.practicum.playlistmaker.data.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    fun getSearchHistory(): SearchHistory

    interface TracksConsumer{
        fun consume(foundTracks: List<Track>)
    }
}