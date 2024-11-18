package com.practicum.playlistmaker.library.data.db

import com.practicum.playlistmaker.library.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.library.data.converters.SavedTrackDbConverter
import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.library.domain.db.PlaylistsRepository
import com.practicum.playlistmaker.search.data.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistsRepositoryImpl(
    private val playlistsDatabase: PlaylistsDatabase,
    private val savedTracksDatabase: SavedTracksDatabase,
    private val savedTracksDbConverter: SavedTrackDbConverter,
    private val playlistDbConverter: PlaylistDbConverter
) : PlaylistsRepository {
    override fun getAllPlaylists(): Flow<List<Playlist>> = flow {
        val playlistsEntity = playlistsDatabase.playlistDao().getPlaylists()
        emit(convertFromPlaylistEntity(playlistsEntity))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConverter.map(playlist) }
    }

    override suspend fun insertPlaylist(playlist: Playlist) {
        playlistsDatabase.playlistDao().insertPlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistsDatabase.playlistDao().updatePlaylist(playlistDbConverter.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        playlistsDatabase.playlistDao().deletePlaylistEntity(playlistDbConverter.map(playlist))
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Int {
        var playlist =
            playlistDbConverter.map(playlistsDatabase.playlistDao().getPlaylistById(playlistId))

        if (isTrackInPlaylist(track.trackId, playlist)) {
            return 0
        } else {
            playlist.addTrackIDtoSavedTracksIDsArray(track.trackId)
            savedTracksDatabase.savedTrackDao().insertTrack(savedTracksDbConverter.map(track))
            updatePlaylist(playlist)
        }
        return 1
    }

    fun isTrackInPlaylist(trackId: Int, playlist: Playlist): Boolean {

        var isTrackInPlaylist = false

        if (playlist.savedTracksIDs.isNullOrEmpty()) {
            isTrackInPlaylist = false
        } else {
            isTrackInPlaylist = playlist.savedTracksIDs?.contains(trackId) == true
        }

        return isTrackInPlaylist
    }

    override suspend fun deleteTrackFromPlaylist(playlistId: Long, trackId: Int) {
        var playlist =
            playlistDbConverter.map(playlistsDatabase.playlistDao().getPlaylistById(playlistId))
        playlist.deleteTrackIDfromSavedTracksIDsArray(trackId)
        updatePlaylist(playlist)
    }

    suspend fun getSavedTracksByPlaylistID(savedIDs: List<Int>): List<Track> {
        return savedTracksDbConverter.map(
            savedTracksDatabase.savedTrackDao().getSavedTracks(savedIDs)
        )
    }

    override suspend fun getSavedTrackIDsOfPlaylist(playlistId: Long): ArrayList<Int>? {
        val playlist =
            playlistDbConverter.map(playlistsDatabase.playlistDao().getPlaylistById(playlistId))
        return playlist.savedTracksIDs
    }

    override suspend fun getSavedTracksByPlaylistID(playlistId: Long): List<Track>? {

        val playlistIDs = getPlaylistByPlaylistId(playlistId).savedTracksIDs
        if (!playlistIDs.isNullOrEmpty()){
            return savedTracksDbConverter.map(
                savedTracksDatabase.savedTrackDao().getSavedTracks(playlistIDs))
        } else return null

    }

    override suspend fun getPlaylistByPlaylistId(playlistId: Long): Playlist {
        var playlist =
            playlistDbConverter.map(playlistsDatabase.playlistDao().getPlaylistById(playlistId))
        return playlist
    }

    override suspend fun createPlaylist(
        artworkUri: String,
        playlistName: String,
        playlistDescription: String?
    ) {
        var playlist: Playlist = Playlist(
            0, artworkUri, playlistName,
            playlistDescription.toString(), 0, null
        )
        playlistsDatabase.playlistDao().insertPlaylist(playlistDbConverter.map(playlist))
    }
}