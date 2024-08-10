package com.practicum.playlistmaker.search.data

import android.content.SharedPreferences
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.ui.JSON_HISTORY_KEY


class SearchHistory(private val sharedPreferences: SharedPreferences
) : SearchHistoryInteractor {

    var json = sharedPreferences.getString(JSON_HISTORY_KEY, null)
    var array = createTrackArrayListFromJson()


    override fun createTrackArrayListFromJson(): ArrayList<Track> {
        json = sharedPreferences.getString(JSON_HISTORY_KEY, null)
        if (!json.isNullOrEmpty()) {
            return Gson().fromJson(json, Array<Track>::class.java).toCollection(ArrayList())
        } else return arrayListOf<Track>()
    }


    private fun createTrackListToJson(tracks: ArrayList<Track>): String {
        return Gson().toJson(tracks)
    }

    private fun saveToHistory(jsonHistory: String) {
        sharedPreferences.edit()
            .putString(JSON_HISTORY_KEY, jsonHistory)
            .apply()
    }

    override fun addTrackToArray(track: Track) {
        if (array.isNullOrEmpty()) {
            array.add(track)
        } else {
            if (array.contains(track)) {
                array.remove(track)
            }

            if (array.size == 10) {
                array.removeAt(9)
                array.add(0, track)
            } else array.add(0, track)
        }


        var jsons = createTrackListToJson(array)
        saveToHistory(jsons)
    }

    override fun cleanHistory() {
        //array.clear()
        sharedPreferences.edit().clear().apply()
    }

    override fun getSharedPrefs(): SharedPreferences{
        return sharedPreferences
    }



}
