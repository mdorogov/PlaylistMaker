package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.data.dto.ItunesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApi {
    @GET("/search")
    suspend fun search(@Query("term") text: String, @Query("entity") entity: String = "song"): ItunesResponse
}