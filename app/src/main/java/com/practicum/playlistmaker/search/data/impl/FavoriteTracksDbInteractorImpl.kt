package com.practicum.playlistmaker.search.data.impl

import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.domain.db.FavoriteTracksDbInteractor
import com.practicum.playlistmaker.search.domain.db.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow

class FavoriteTracksDbInteractorImpl(private val favoriteTracksRepository: FavoriteTracksRepository) :
    FavoriteTracksDbInteractor {
    override fun favoriteTracks(): Flow<List<Track>> {
        return favoriteTracksRepository.favoriteTracks()
    }

    override suspend fun deleteFavTrack(idFavTrack: Track) {
        favoriteTracksRepository.deleteFavTrack(idFavTrack)
    }

    override suspend fun insertFavTrack(favTrack: Track) {
        favoriteTracksRepository.insertFavTrack(favTrack)
    }

    override suspend fun isTrackFavorite(trackId: Int): Boolean {
        return favoriteTracksRepository.isTrackFavorite(trackId)
    }
}