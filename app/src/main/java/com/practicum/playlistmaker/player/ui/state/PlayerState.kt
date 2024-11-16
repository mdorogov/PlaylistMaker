package com.practicum.playlistmaker.player.ui.state

import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.search.data.models.Track

sealed class PlayerState {
    object Loading : PlayerState()

    data class Content(
        val trackModel: Track,
        val isTrackFavorite: Boolean
    ) : PlayerState()

    data class FavoriteTrackChanged(val isTrackFavorite: Boolean) : PlayerState()
    data class PlaylistChoosing(val playlists: ArrayList<Playlist>) : PlayerState()
    data class NewPlaylistCreated(val isPlaylistCreatingPrepared: Boolean) : PlayerState()
    data class TrackIsAdded(
        val isTrackAdded: Boolean,
        val nameOfTrack: String,
        val nameOfPlaylist: String
    ) : PlayerState()

    data class PlaylistUpdate(val playlists: ArrayList<Playlist>) : PlayerState()

    data class PlayTime(val progress: String) : PlayerState()
    data class PlayTimePaused(val progress: String) : PlayerState()
    data class PlayingStopped(val progress: String) : PlayerState()


}