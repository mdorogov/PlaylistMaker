package com.practicum.playlistmaker.search.data.converters

import com.practicum.playlistmaker.search.data.db.entity.TrackEntity
import com.practicum.playlistmaker.search.data.dto.TrackDto
import com.practicum.playlistmaker.search.data.models.Track

class TrackDbConverter {

    fun map(track: Track): TrackEntity {
        return TrackEntity(track.trackId, track.trackName, track.artistName,
            track.trackTimeMillis, track.artworkUrl100, track.previewUrl, track.collectionName, track.primaryGenreName,
            track.country, track.releaseDate)
    }

    fun map(track: TrackEntity): Track {
        return Track(track.trackName, track.artistName,
            track.trackTimeMillis, track.artworkUrl100, track.trackId, track.previewUrl, track.collectionName, track.primaryGenreName,
            track.country, track.releaseDate)
    }
}