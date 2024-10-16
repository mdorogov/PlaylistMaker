package com.practicum.playlistmaker.search.domain.db

import com.practicum.playlistmaker.search.data.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksRepository {
    fun favoriteTracks(): Flow<List<Track>>
    suspend fun insertFavTrack(favTrack: Track)
    suspend fun deleteFavTrack(favTrack: Track)
    suspend fun isTrackFavorite(trackId: Int) : Boolean
}