package com.practicum.playlistmaker.library.ui.state

import com.practicum.playlistmaker.library.data.models.Playlist

sealed interface PlaylistState {
    data class Content(val playlist: Playlist): PlaylistState

}