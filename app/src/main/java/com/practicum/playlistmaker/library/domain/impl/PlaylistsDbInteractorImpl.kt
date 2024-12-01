package com.practicum.playlistmaker.library.domain.impl

import android.net.Uri
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

    override suspend fun deletePlaylist(playlistId: Long) {
        playlistsRepository.deletePlaylist(playlistId)
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Int {
        return playlistsRepository.addTrackToPlaylist(playlistId, track)
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int) {
        playlistsRepository.deleteTrackFromPlaylist(playlistId, trackId)
    }

    override suspend fun getSavedTracksByPlaylistID(playlistId: Long): Pair<List<Track>?, Long> {
        return playlistsRepository.getSavedTracksByPlaylistID(playlistId)
    }

    override suspend fun createPlaylist(
        artworkUri: Uri?,
        playlistName: String,
        playlistDescription: String?
    ) {
        playlistsRepository.createPlaylist(artworkUri, playlistName, playlistDescription)
    }

    override suspend fun getPlaylistByPlaylistId(playlistId: Long): Playlist {
        return playlistsRepository.getPlaylistByPlaylistId(playlistId)
    }

    override suspend fun updatePlaylist(
        playlistId: Long,
        playlistArtwork: Uri?,
        playlistName: String,
        playlistDesctription: String
    ) {
        playlistsRepository.updatePlaylistData(
            playlistId,
            playlistArtwork,
            playlistName,
            playlistDesctription
        )
    }
}