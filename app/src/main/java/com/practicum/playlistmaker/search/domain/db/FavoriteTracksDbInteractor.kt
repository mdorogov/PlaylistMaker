package com.practicum.playlistmaker.search.domain.db

import com.practicum.playlistmaker.search.data.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksDbInteractor {
    fun favoriteTracks(): Flow<List<Track>>
    suspend fun insertFavTrack(favTrack: Track)
    suspend fun deleteFavTrack(FavTrack: Track)
    suspend fun isTrackFavorite(trackId: Int): Boolean
}