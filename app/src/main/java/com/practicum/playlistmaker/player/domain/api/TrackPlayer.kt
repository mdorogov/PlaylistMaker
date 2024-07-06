package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.search.data.models.Track

interface TrackPlayer {
    fun play(previewUrl: String, statusObserver: StatusObserver)
    fun pause()
    fun seek(str: String)
    fun resume()
fun release()
fun initializePlayer(previewUrl: String)


interface StatusObserver {
    fun onProgress(progress: String)
    fun onStop()
    fun onPlay()
    fun onPause()
}
}