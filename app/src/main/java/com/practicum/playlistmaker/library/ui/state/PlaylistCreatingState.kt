package com.practicum.playlistmaker.library.ui.state

import com.practicum.playlistmaker.library.data.models.Playlist
import java.net.URI


sealed interface PlaylistCreatingState {
    data class PlaylistCreated(val playlistName: String) : PlaylistCreatingState
    data class PlaylistEdit(val playlist: Playlist): PlaylistCreatingState
    data class PlaylistIsUpdated(val isUpdated: Boolean): PlaylistCreatingState
}