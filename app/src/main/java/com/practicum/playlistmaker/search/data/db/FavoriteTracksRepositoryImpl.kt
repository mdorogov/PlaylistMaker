package com.practicum.playlistmaker.search.data.db

import com.practicum.playlistmaker.search.data.converters.TrackDbConverter
import com.practicum.playlistmaker.search.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.domain.db.FavoriteTracksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTracksRepositoryImpl(
    private val favoriteTracksDatabase: FavoriteTracksDatabase,
    private val trackDbConverter: TrackDbConverter
) : FavoriteTracksRepository {

    override fun favoriteTracks(): Flow<List<Track>> = flow {
        val favTracks = favoriteTracksDatabase.trackDao().getFavTracks()
        emit(convertFromTrackEntityToList(favTracks))
    }

    private fun convertFromTrackEntityToList(favTracks: List<TrackEntity>): List<Track> {
        return favTracks.map { trackEntity -> trackDbConverter.map(trackEntity) }
    }

    override suspend fun deleteFavTrack(favTrack: Track) {
        favoriteTracksDatabase.trackDao().deleteTrackEntity(trackDbConverter.map(favTrack))
    }

    override suspend fun insertFavTrack(favTrack: Track) {
        val favTrackEntity = trackDbConverter.map(favTrack)
        favTrackEntity.timeStamp = System.currentTimeMillis().toString()
        favoriteTracksDatabase.trackDao().insertTrackEntity(favTrackEntity)
    }

    override suspend fun isTrackFavorite(trackId: Int): Boolean {
        if (favoriteTracksDatabase.trackDao().isThereTrack(trackId = trackId.toString()) == 0) {
            return false
        } else return true
    }
}

