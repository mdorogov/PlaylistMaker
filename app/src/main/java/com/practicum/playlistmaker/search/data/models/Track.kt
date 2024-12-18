package com.practicum.playlistmaker.search.data.models

import com.practicum.playlistmaker.search.mapper.MillisConverter

data class Track(
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
    val timeStamp: String
) {
    fun getPlayerArtwork(): String {
        return artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }
    fun getPlayerTrackTime(): String{
        return MillisConverter.millisToMinutesAndSeconds(trackTimeMillis)
    }

    fun setTimeStamp(): String {
        return System.nanoTime().toString()
    }

    fun convertTrackToString(): String {
        val trackDuration = MillisConverter.millisToMinutesAndSeconds(trackTimeMillis)
        return "$artistName - $trackName ($trackDuration)"
    }
}