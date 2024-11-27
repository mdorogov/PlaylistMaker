package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.search.data.models.Track

interface TracksPlayerInteractor {
    fun loadPlayerData(json: String): Track
    suspend fun play()
    fun pause()
    fun resume()
    fun release()
    fun getPlayingStatus(): Boolean
    fun getCurrentPlayingPosition(): String
    fun getIsSongPlayed(): Boolean
    fun initializePlayer(previewUrl: String)


}