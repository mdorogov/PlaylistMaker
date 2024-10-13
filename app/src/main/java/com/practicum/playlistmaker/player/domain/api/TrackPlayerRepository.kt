package com.practicum.playlistmaker.player.domain.api

interface TrackPlayerRepository {
    fun play(previewUrl: String)
    fun pause()
    fun seek(str: String)
    fun resume()
    fun releasePlayer()
    fun initializePlayer(previewUrl: String)
    fun getPlayingStatus(): Boolean
    fun updateCurrentPlaybackTime(): String
    fun getIsSongPlayed(): Boolean


    interface StatusObserver {
        fun onProgress(progress: String)
        fun onStop()
        fun onPlay()
        fun onPause(progress: String)
    }
}