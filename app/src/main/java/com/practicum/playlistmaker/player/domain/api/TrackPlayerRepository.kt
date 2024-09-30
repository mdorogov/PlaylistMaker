package com.practicum.playlistmaker.player.domain.api

interface TrackPlayerRepository {
    fun play1(previewUrl: String, statusObserver: StatusObserver)
    fun play(previewUrl: String)
    fun pause()
    fun seek(str: String)
    fun resume()
    fun release()
    fun initializePlayer(previewUrl: String)
    fun getPlayingStatus(): Boolean
    fun updateCurrentPlaybackTime(): String


    interface StatusObserver {
        fun onProgress(progress: String)
        fun onStop()
        fun onPlay()
        fun onPause(progress: String)
    }
}