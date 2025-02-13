package com.practicum.playlistmaker.library.ui.state

import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.search.data.models.Track

sealed interface PlaylistState {
    data class Content(val playlist: Playlist, val tracks: List<Track>?, val tracksDuration: Long): PlaylistState
    data class TrackIsDeleted(val boolean: Boolean): PlaylistState
    data class PlaylistIsDeleted(val booalean: Boolean): PlaylistState

}