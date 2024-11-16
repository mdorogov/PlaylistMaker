package com.practicum.playlistmaker.library.ui.state

import java.net.URI


sealed interface PlaylistCreatingState {
    data class PlaylistCreated(val playlistName: String) : PlaylistCreatingState
}