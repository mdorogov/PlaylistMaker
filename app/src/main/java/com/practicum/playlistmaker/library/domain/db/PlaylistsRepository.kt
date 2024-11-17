package com.practicum.playlistmaker.library.domain.db

import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.search.data.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun insertPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Int
    suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int)
    suspend fun getSavedTrackIDsOfPlaylist(playlistId: Long): ArrayList<Int>?
    suspend fun getSavedTracksByPlaylistID(playlistId: Long): List<Track>
    suspend fun createPlaylist(artworkUri: String, playlistName: String, playlistDescription: String?)
    suspend fun getPlaylistByPlaylistId(playlistId: Long): Playlist
}