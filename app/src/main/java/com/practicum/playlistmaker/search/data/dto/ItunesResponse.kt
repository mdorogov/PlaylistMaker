package com.practicum.playlistmaker.search.data.dto

class ItunesResponse(val resultCount: String,
    val results:MutableList<TrackDto>) : Response()