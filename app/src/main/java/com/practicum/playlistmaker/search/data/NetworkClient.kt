package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.data.dto.ItunesRequest
import com.practicum.playlistmaker.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: ItunesRequest): Response
}