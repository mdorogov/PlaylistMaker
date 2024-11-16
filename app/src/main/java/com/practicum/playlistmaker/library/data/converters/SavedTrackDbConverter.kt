package com.practicum.playlistmaker.library.data.converters

import com.practicum.playlistmaker.library.data.db.entity.SavedTrackEntity
import com.practicum.playlistmaker.search.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.data.models.Track

class SavedTrackDbConverter {
    fun map(track: Track): SavedTrackEntity {
        return SavedTrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.primaryGenreName,
            track.country,
            track.releaseDate,
            track.previewUrl,
            track.timeStamp
        )
    }

    fun map(track: SavedTrackEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.primaryGenreName,
            track.country,
            track.releaseDate,
            track.previewUrl,
            track.timeStamp
        )
    }

    fun map(entityArray: List<SavedTrackEntity>): List<Track> {
        var tracks = arrayListOf<Track>()

        for (entity in entityArray) {
            tracks.add(map(entity))
        }
        return tracks
    }
}