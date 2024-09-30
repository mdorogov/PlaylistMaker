package com.practicum.playlistmaker.player.domain.impl

import com.google.gson.Gson
import com.practicum.playlistmaker.player.domain.api.TrackPlayerRepository
import com.practicum.playlistmaker.player.domain.api.TracksPlayerInteractor
import com.practicum.playlistmaker.search.data.models.Track

class TracksPlayerInteractorImpl(private val trackPlayerRepository: TrackPlayerRepository) : TracksPlayerInteractor {
    override fun loadPlayerData(json: String): Track {
        return Gson().fromJson(json, Track::class.java)
    }

    override fun pause() {
        trackPlayerRepository.pause()
    }

    override fun resume() {
        trackPlayerRepository.resume()
    }

    override fun play1(previewUrl: String, statusObserver: TrackPlayerRepository.StatusObserver) {
       trackPlayerRepository.play1(previewUrl,statusObserver)
    }
    override fun play(previewUrl: String) {
        trackPlayerRepository.play(previewUrl)
    }

    override fun release() {
        trackPlayerRepository.release()
    }

    override fun getPlayingStatus(): Boolean {
        return trackPlayerRepository.getPlayingStatus()
    }
    override fun getCurrentPlayingPosition(): String{
        return trackPlayerRepository.updateCurrentPlaybackTime()
    }
}