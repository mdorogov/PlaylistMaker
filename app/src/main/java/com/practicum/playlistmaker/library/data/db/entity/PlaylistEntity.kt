package com.practicum.playlistmaker.library.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val artwork: String,
    val playlistName: String,
    val description: String,
    val numOfTracks: Int,
    var jsonTrackIDs: String
)