package com.practicum.playlistmaker.library.ui.state

import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.ui.state.SearchState

sealed interface FavoriteTracksState {
    data class Content(val favoriteTracks: List<Track>) : FavoriteTracksState
}