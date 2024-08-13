package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.search.data.models.Track

interface TracksPlayerInteractor {
    fun loadPlayerData(json: String): Track
    fun play(previewUrl: String, statusObserver: TrackPlayerRepository.StatusObserver)
    fun pause()
    fun resume()
    fun release()

}