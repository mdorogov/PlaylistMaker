package com.practicum.playlistmaker.domain.api

import com.practicum.playlistmaker.data.SearchHistory
import com.practicum.playlistmaker.data.models.Track

interface TrackRepository {

    fun getSearchHistoryHandler(): SearchHistory

    fun getSharedPrefTracks(): List<Track>
    fun searchTrack(expression: String): List<Track>
}