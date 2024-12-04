package com.practicum.playlistmaker.library.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.practicum.playlistmaker.library.data.converters.PlaylistDbConverter
import com.practicum.playlistmaker.library.data.converters.SavedTrackDbConverter
import com.practicum.playlistmaker.library.data.db.PlaylistsDatabase
import com.practicum.playlistmaker.library.data.db.SavedTracksDatabase
import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.library.domain.db.PlaylistsRepository
import com.practicum.playlistmaker.search.data.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream

class PlaylistsRepositoryImpl(
    private val context: Context,
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

    suspend fun updatePlaylist(playlist: PlaylistEntity) {
        playlistsDatabase.playlistDao().updatePlaylist(playlist)
    }


    override suspend fun updatePlaylistData(
        playlistId: Long,
        playlistArtwork: Uri?,
        playlistName: String,
        playlistDesctription: String
    ) {
        var finalUri = ""
        val playlist = PlaylistDbConverter().map(playlistsDatabase.playlistDao().getPlaylistById(playlistId))
        if (playlist.artwork.equals(playlistArtwork.toString())){
            finalUri = playlistArtwork.toString()
        } else finalUri = getFinalStringUri(playlistArtwork)
        updatePlaylist(Playlist(playlistId, finalUri, playlistName, playlistDesctription, playlist.numOfTracks, playlist.savedTracksIDs))

    }

    override suspend fun deletePlaylist(playlistId: Long) {
        val savedTracks: ArrayList<Int>? = getSavedTrackIDsOfPlaylist(playlistId)
        playlistsDatabase.playlistDao().deletePlaylist(playlistId)
        if (!savedTracks.isNullOrEmpty()) updateAllSavedTracks(savedTracks)
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

        updateSavedTracks(trackId)
    }

    private suspend fun updateSavedTracks(trackId: Int) {
        var playlists = convertFromPlaylistEntity(playlistsDatabase.playlistDao().getPlaylists())
        var isTrackSaved = false
        for (playlist in playlists) {
            if (!playlist.savedTracksIDs.isNullOrEmpty()) {
                if (playlist.savedTracksIDs?.contains(trackId) == true) {
                    isTrackSaved = true
                    break
                }
            }
        }

        if (!isTrackSaved) savedTracksDatabase.savedTrackDao().deleteSavedTrackById(trackId)
    }

    private suspend fun updateAllSavedTracks(deletedPlaylistIDs: ArrayList<Int>) {
        var playlists = convertFromPlaylistEntity(playlistsDatabase.playlistDao().getPlaylists())

        for (trackId in deletedPlaylistIDs) {
            var isTrackSaved = false

            for (playlist in playlists) {
                if (playlist.savedTracksIDs?.contains(trackId) == true) {
                    isTrackSaved = true
                    break;
                }
                if (isTrackSaved) break
            }

            if (!isTrackSaved) savedTracksDatabase.savedTrackDao().deleteSavedTrackById(trackId)
        }
    }

    override suspend fun getSavedTrackIDsOfPlaylist(playlistId: Long): ArrayList<Int>? {
        val playlist =
            playlistDbConverter.map(playlistsDatabase.playlistDao().getPlaylistById(playlistId))
        return playlist.savedTracksIDs
    }

    override suspend fun getSavedTracksByPlaylistID(playlistId: Long): Pair<List<Track>?, Long> {

        val playlistIDs = getPlaylistByPlaylistId(playlistId).savedTracksIDs
        if (!playlistIDs.isNullOrEmpty()) {
            return savedTracksDbConverter.map(
                savedTracksDatabase.savedTrackDao().getSavedTracks(playlistIDs), playlistIDs
            )
        } else return Pair(listOf<Track>(), 0)
    }

    override suspend fun getPlaylistByPlaylistId(playlistId: Long): Playlist {
        var playlist =
            playlistDbConverter.map(playlistsDatabase.playlistDao().getPlaylistById(playlistId))
        return playlist
    }

    override suspend fun createPlaylist(
        artworkUri: Uri?,
        playlistName: String,
        playlistDescription: String?
    ) {

        val finalUri = getFinalStringUri(artworkUri)

        var playlist: Playlist = Playlist(
            0, finalUri, playlistName,
            playlistDescription.toString(), 0, null
        )
        playlistsDatabase.playlistDao().insertPlaylist(playlistDbConverter.map(playlist))
    }

    private fun getFinalStringUri(artworkUri: Uri?): String {
        if (artworkUri != null) {
            val filePath = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "artworks"
            )

            if (!filePath.exists()) {
                filePath.mkdirs()
            }

            val file = File(filePath, "art_" + System.currentTimeMillis().toString())
            val inputStream = context.contentResolver.openInputStream(artworkUri)
            val outputStream = FileOutputStream(file)
            BitmapFactory.decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

            return file.absolutePath
        } else {
            return "android.resource://com.practicum.playlistmaker/drawable/artwork_placeholder"
        }
    }


}