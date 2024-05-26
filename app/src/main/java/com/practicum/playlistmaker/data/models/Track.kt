package com.practicum.playlistmaker.data.models

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: String,
    val artworkUrl100: String,
    val trackId: Int,
    val collectionName: String,
    val primaryGenreName: String,
    val country: String,
    val releaseDate: String,
    val previewUrl: String,
) {
    fun getPlayerArtwork(): String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }
}