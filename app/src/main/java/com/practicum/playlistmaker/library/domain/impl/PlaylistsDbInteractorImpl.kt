package com.practicum.playlistmaker.library.domain.impl

import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.library.domain.db.PlaylistsDbInteractor
import com.practicum.playlistmaker.library.domain.db.PlaylistsRepository
import com.practicum.playlistmaker.search.data.models.Track
import kotlinx.coroutines.flow.Flow

class PlaylistsDbInteractorImpl(private val playlistsRepository: PlaylistsRepository) :
    PlaylistsDbInteractor {
    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistsRepository.getAllPlaylists()
    }

    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistsRepository.insertPlaylist(playlist)
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistsRepository.deletePlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Int {
        return playlistsRepository.addTrackToPlaylist(playlistId, track)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int) {
        playlistsRepository.deleteTrackFromPlaylist(playlistId, trackId)
    }

    override suspend fun getSavedTracksByPlaylistID(playlistId: Long) {
        playlistsRepository.getSavedTracksByPlaylistID(playlistId)
    }

    override suspend fun createPlaylist(
        artworkUri: String,
        playlistName: String,
        playlistDescription: String?
    ) {
        playlistsRepository.createPlaylist(artworkUri, playlistName, playlistDescription)
    }

    override suspend fun getPlaylistByPlaylistId(playlistId: Long): Playlist {
        return playlistsRepository.getPlaylistByPlaylistId(playlistId)
    }
}