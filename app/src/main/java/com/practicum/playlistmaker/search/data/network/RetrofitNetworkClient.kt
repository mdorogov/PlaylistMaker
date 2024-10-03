package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.ItunesRequest
import com.practicum.playlistmaker.search.data.dto.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RetrofitNetworkClient(
    private val itunesService: ItunesApi
): NetworkClient {

    override suspend fun doRequest(dto: ItunesRequest): Response {


        return withContext(Dispatchers.IO) {
            try {
                val resp = itunesService.search(dto.expression)
                resp.apply {
                    resultCode = 200
                }
                } catch (ex: Exception) {
                Response().apply { resultCode = 400 }
            }
        }
    }
}