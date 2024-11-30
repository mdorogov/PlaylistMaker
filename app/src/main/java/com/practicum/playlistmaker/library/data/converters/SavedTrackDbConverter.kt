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

    fun map(entityArray: List<SavedTrackEntity>, playlistIDs: List<Int>): Pair<List<Track>, Long> {
        var tracks = arrayListOf<Track>()
        var durationOfAllTracks: Long = 0

        for (entity in entityArray) {
            val track: Track = map(entity)
            var trackDuration: Long? = track.trackTimeMillis.toLongOrNull()
            if (trackDuration != null) durationOfAllTracks += trackDuration

            tracks.add(map(entity))
        }

        val trackSort = tracks.associateBy { it.trackId }
        val sortedTracks = playlistIDs.reversed().mapNotNull { trackSort[it] }
        return Pair(sortedTracks, durationOfAllTracks)
    }
}