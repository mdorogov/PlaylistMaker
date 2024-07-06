package com.practicum.playlistmaker.player.ui.state

import com.practicum.playlistmaker.search.data.models.Track

sealed class PlayerState {
    object Loading: PlayerState()

    data class Content(
        val trackModel: Track,): PlayerState()

}