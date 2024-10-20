package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.search.data.models.Track

interface TracksPlayerInteractor {
    fun loadPlayerData(json: String): Track
    suspend fun play(previewUrl: String)
    fun pause()
    fun resume()
    fun release()
    fun getPlayingStatus(): Boolean
    fun getCurrentPlayingPosition(): String
    fun getIsSongPlayed(): Boolean


}