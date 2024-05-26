package com.practicum.playlistmaker.data.network

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.data.NetworkClient
import com.practicum.playlistmaker.data.SearchHistory
import com.practicum.playlistmaker.data.dto.ItunesRequest
import com.practicum.playlistmaker.data.dto.ItunesResponse
import com.practicum.playlistmaker.data.models.Track
import com.practicum.playlistmaker.domain.api.TrackRepository

const val SEARCH_TRACK_HISTORY = "search_track_history"
const val JSON_HISTORY_KEY = "key_for_json_history"
class TrackRepositoryImpl(private val networkClient: NetworkClient, private val sharedPreferences: SharedPreferences) : TrackRepository {



override fun getSearchHistoryHandler(): SearchHistory{
    return SearchHistory(sharedPreferences)
}
    override fun getSharedPrefTracks(): List<Track> {
        return SearchHistory(sharedPreferences).createTrackArrayListFromJson()
    }
    override fun searchTrack(expression: String): List<Track> {
        val response = networkClient.doRequest(ItunesRequest(expression))

        if (response.resultCode == 200) {
            return (response as ItunesResponse).results.map {
                Track(it.trackName, it.artistName, it.trackTimeMillis, it.artworkUrl100,it.trackId,
                    it.collectionName, it.primaryGenreName, it.country, it.releaseDate, it.previewUrl)}
        } else {
            return emptyList()
        }

    }
}