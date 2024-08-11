package com.practicum.playlistmaker.player.domain.impl

import com.google.gson.Gson
import com.practicum.playlistmaker.player.domain.api.TracksPlayerInteractor
import com.practicum.playlistmaker.search.data.models.Track

class TracksPlayerInteractorImpl() : TracksPlayerInteractor {
    override fun loadPlayerData(json: String): Track {
        return Gson().fromJson(json, Track::class.java)
    }
}