package com.practicum.playlistmaker.library.data.converters

import com.google.gson.Gson
import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.library.data.models.Playlist

class PlaylistDbConverter {

    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.artwork,
            playlist.playlistName,
            playlist.description,
            playlist.numOfTracks,
            Gson().toJson(playlist.savedTracksIDs)
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id,
            playlist.artwork,
            playlist.playlistName,
            playlist.description,
            playlist.numOfTracks,
            getSavedJsonTracksIDs(playlist.jsonTrackIDs)
        )
    }

    fun savedTracksIDsArrayToJsonId(savedTracks: List<Int>): String? {
        if (savedTracks != null) {
            return Gson().toJson(savedTracks)
        } else return null


    }

    fun getSavedJsonTracksIDs(json: String): ArrayList<Int>? {
        if (json.equals("null")) {
            return arrayListOf()
        } else return Gson().fromJson(json, Array<Int>::class.java).toCollection(ArrayList<Int>())
    }
}
