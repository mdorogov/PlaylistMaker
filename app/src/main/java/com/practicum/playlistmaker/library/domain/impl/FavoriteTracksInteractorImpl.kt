package com.practicum.playlistmaker.library.domain.impl

import com.practicum.playlistmaker.library.domain.api.FavoriteTracksInteractor
import com.practicum.playlistmaker.search.data.models.Track

class FavoriteTracksInteractorImpl: FavoriteTracksInteractor {
    override fun getFavTracks(): ArrayList<Track> {
        return ArrayList()
    }
}