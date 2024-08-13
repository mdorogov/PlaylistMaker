package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.search.data.models.Track

interface TrackSearchInteractor {

    fun searchTrack(expression: String): Resource<List<Track>?>

}