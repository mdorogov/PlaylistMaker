package com.practicum.playlistmaker.search.data.dto

data class TrackDto(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val trackId: Int,
    val collectionName: String,
    val primaryGenreName: String,
    val country: String,
    val releaseDate: String,
    val previewUrl: String
) {
}