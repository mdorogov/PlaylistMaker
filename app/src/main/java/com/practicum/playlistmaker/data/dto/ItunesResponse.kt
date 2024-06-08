package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.data.models.Track

class ItunesResponse(val resultCount: String,
    val results:MutableList<TrackDto>) : Response()