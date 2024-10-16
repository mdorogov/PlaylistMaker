package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.dto.ItunesRequest
import com.practicum.playlistmaker.search.data.dto.ItunesResponse
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.domain.api.TrackSearchInteractor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

const val SEARCH_TRACK_HISTORY = "search_track_history"
const val JSON_HISTORY_KEY = "key_for_json_history"
class TrackSearchInteractorImpl(private val networkClient: NetworkClient) :
    TrackSearchInteractor {

    override fun searchTrack(expression: String): Flow<Resource<List<Track>?>> = flow {
        val response = networkClient.doRequest(ItunesRequest(expression))

        when(response.resultCode) {
            -1 -> {
                emit(Resource.Error("No Connection"))
            }
            200 -> {
                with(response as ItunesResponse) {
                    val data = results.map {
                        Track(it.trackId,it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100,
                            it.collectionName, it.primaryGenreName, it.country, it.releaseDate, it.previewUrl, it.trackTimeMillis,)
                    }
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error(response.resultCode.toString()))
            }
        }

    }

}