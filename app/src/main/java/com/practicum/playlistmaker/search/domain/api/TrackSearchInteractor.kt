package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.search.data.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackSearchInteractor {

    fun searchTrack(expression: String): Flow<Resource<List<Track>?>>

}