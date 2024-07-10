package com.practicum.playlistmaker.search.ui.state

import com.practicum.playlistmaker.search.data.SearchHistory
import com.practicum.playlistmaker.search.data.models.Track

sealed interface SearchState {
    object Loading : SearchState

    data class Content(val foundTracks: List<Track>) : SearchState
    data class History(val historyTracks: List<Track>) : SearchState

    data class Error(val errorMessage: String, val userRequest: String) : SearchState

    data class Empty(val message: String, val userRequest: String) : SearchState
    //data class providingSearchHistory(val searchHistory: SearchHistory) : SearchState
}