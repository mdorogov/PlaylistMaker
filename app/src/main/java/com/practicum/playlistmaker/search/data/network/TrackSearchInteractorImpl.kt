package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.ItunesRequest
import com.practicum.playlistmaker.search.data.dto.ItunesResponse
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.domain.api.TrackSearchInteractor

const val SEARCH_TRACK_HISTORY = "search_track_history"
const val JSON_HISTORY_KEY = "key_for_json_history"
class TrackSearchInteractorImpl(private val networkClient: NetworkClient) :
    TrackSearchInteractor {

    override fun searchTrack(expression: String): Resource<List<Track>?> {
        val response = networkClient.doRequest(ItunesRequest(expression))

        return when(response.resultCode) {
            -1 -> {
                Resource.Error("No Connection")
            }
            200 -> {
                Resource.Success((response as ItunesResponse).results.map {
                    Track(it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100,it.trackId,
                        it.collectionName, it.primaryGenreName, it.country, it.releaseDate, it.previewUrl)
                })
            }
            else -> {
                Resource.Error(response.resultCode.toString())
            }
        }

    }

}