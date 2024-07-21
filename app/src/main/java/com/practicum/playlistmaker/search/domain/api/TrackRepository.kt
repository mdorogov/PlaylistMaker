package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.search.data.SearchHistory
import com.practicum.playlistmaker.search.data.models.Track

interface TrackRepository {

    fun getSearchHistoryHandler(): SearchHistory

    fun getSharedPrefTracks(): List<Track>
    fun searchTrack(expression: String): Resource<List<Track>?>

}