package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.search.data.models.Track

interface TracksPlayerInteractor {
    fun loadPlayerData(json: String): Track

}