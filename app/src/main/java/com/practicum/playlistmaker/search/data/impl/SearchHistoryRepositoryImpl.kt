package com.practicum.playlistmaker.search.data.impl

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.data.network.JSON_HISTORY_KEY
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import kotlin.math.truncate


class SearchHistoryRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
) : SearchHistoryRepository {

    var json = sharedPreferences.getString(JSON_HISTORY_KEY, null)
    var array = createTrackArrayListFromJson()

    private val _historyChanged = MutableLiveData<Boolean>()
    val historyChanged: LiveData<Boolean> get() = _historyChanged

    private val sharedPrefListener = SharedPreferences.OnSharedPreferenceChangeListener{sharedPreferences,JSON_HISTORY_KEY ->
        _historyChanged.postValue(true)
    }
    init {
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPrefListener)
    }

    override fun getChangedHistoryLiveData(): LiveData<Boolean> {
        return historyChanged
    }


    override fun createTrackArrayListFromJson(): ArrayList<Track> {
        json = sharedPreferences.getString(JSON_HISTORY_KEY, null)
        if (!json.isNullOrEmpty()) {
            return Gson().fromJson(json, Array<Track>::class.java).toCollection(ArrayList())
        } else return arrayListOf<Track>()
    }


    override fun createTrackListToJson(tracks: ArrayList<Track>): String {
        return Gson().toJson(tracks)
    }

    override fun saveToHistory(jsonHistory: String) {
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
        sharedPreferences.edit().clear().apply()
    }
}
