package com.practicum.playlistmaker.library.domain.api

import com.practicum.playlistmaker.search.data.models.Track

interface FavoriteTracksInteractor {

    fun getFavTracks(): ArrayList<Track>
}