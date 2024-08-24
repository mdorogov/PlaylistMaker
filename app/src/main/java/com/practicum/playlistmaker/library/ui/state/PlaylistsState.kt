package com.practicum.playlistmaker.library.ui.state

import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.ui.state.SearchState

sealed interface PlaylistsState {
    data class Content(val playlists: List<Playlist>) : PlaylistsState
    data class ContentNotFound(val stringRes: Int): PlaylistsState
}