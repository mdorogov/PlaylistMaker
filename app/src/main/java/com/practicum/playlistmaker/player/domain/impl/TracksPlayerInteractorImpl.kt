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

    override fun play(previewUrl: String, statusObserver: TrackPlayerRepository.StatusObserver) {
       trackPlayerRepository.play(previewUrl,statusObserver)
    }

    override fun release() {
        trackPlayerRepository.release()
    }
}