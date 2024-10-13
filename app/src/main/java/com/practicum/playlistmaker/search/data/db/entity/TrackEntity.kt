package com.practicum.playlistmaker.search.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(tableName = "favorite_tracks")
data class TrackEntity(
    @PrimaryKey @ColumnInfo(name = "track_id")
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val collectionName: String,
    val primaryGenreName: String,
    val country: String,
    val releaseDate: String,
    val previewUrl: String,
    var timeStamp: String,
)
