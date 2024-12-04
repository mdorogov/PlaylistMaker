package com.practicum.playlistmaker.library.domain.db

import android.net.Uri
import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.search.data.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsDbInteractor {
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlistId: Long)
    suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Int
    suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int)
    suspend fun getSavedTracksByPlaylistID(playlistId: Long): Pair<List<Track>?, Long>
    suspend fun createPlaylist(
        artworkUri: Uri?,
        playlistName: String,
        playlistDescription: String?
    )

    suspend fun getPlaylistByPlaylistId(playlistId: Long): Playlist
    suspend fun updatePlaylist(
        playlist: Long,
        playlistArtwork: Uri?,
        playlistName: String,
        playlistDesctription: String
    )
}