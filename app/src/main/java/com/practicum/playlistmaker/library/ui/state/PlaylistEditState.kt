package com.practicum.playlistmaker.library.ui.state

import com.practicum.playlistmaker.library.data.models.Playlist

sealed interface PlaylistEditState {
    data class PlaylistDataLoaded(val playlist: Playlist): PlaylistEditState
}