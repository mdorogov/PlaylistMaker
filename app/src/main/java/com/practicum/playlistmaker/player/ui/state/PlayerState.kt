package com.practicum.playlistmaker.player.ui.state

import com.practicum.playlistmaker.search.data.models.Track

sealed class PlayerState {
    object Loading : PlayerState()

    data class Content(
        val trackModel: Track,
    ) : PlayerState()

    data class FavoriteTrackChanged(val isTrackFavorite: Boolean) : PlayerState()

    data class PlayTime(val progress: String) : PlayerState()
    data class PlayTimePaused(val progress: String) : PlayerState()
    data class PlayingStopped(val progress: String) : PlayerState()
}