package com.practicum.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    var artwork: String,
    var playlistName: String,
    var description: String,
    val numOfTracks: Int,
    var jsonTrackIDs: String
)