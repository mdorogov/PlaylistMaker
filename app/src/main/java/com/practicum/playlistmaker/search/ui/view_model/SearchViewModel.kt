package com.practicum.playlistmaker.search.ui.view_model

import android.app.Application
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.data.network.JSON_HISTORY_KEY
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.ui.state.SearchState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SearchViewModel(
    application: Application,
    private val tracksInteractor: TracksInteractor
) : AndroidViewModel(application) {
    private val handler = Handler(Looper.getMainLooper())

    private var stateLiveData = MutableLiveData<SearchState>()
    private var latestUserRequest: String? = null
    private var searchDebounceJob: Job? = null


    companion object {

        private val SEARCH_REQUEST_TOKEN = Any()
        private const val AUTO_SEARCHING_DELAY = 2000L
    }


    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }


    fun observeState(): LiveData<SearchState> = stateLiveData

    fun searchTracks(userRequest: String) {
        if (userRequest.isNotEmpty()) {
            renderState(SearchState.Loading)
            viewModelScope.launch {
                tracksInteractor.searchTracks(userRequest)
                    .collect { pair ->
                        processResult(pair.first, pair.second, userRequest)
                    }
            }
        } else {
            renderState(SearchState.Error("Not Found", userRequest))
        }
    }

    private fun processResult(
        foundTracks: List<Track>?,
        errorMessage: String?,
        userRequest: String
    ) {
        if (foundTracks != null) {
            renderState(SearchState.Content(foundTracks))
        }

        if (errorMessage != null) {
            renderState(
                SearchState.Error(
                    errorMessage = "No Connection",
                    userRequest = userRequest
                )
            )
        } else if (foundTracks.isNullOrEmpty()) {
            renderState(
                SearchState.Empty(
                    message = "Not Found",
                    userRequest = userRequest
                )
            )
        }
    }

    fun setHistorySharedPrefListener() {
        tracksInteractor.getChangedHistoryLiveData().observeForever {
            historyChanged ->
            if (historyChanged){
                loadTracksHistory()
            }
        }
    }


    fun renderState(state: SearchState) {
        stateLiveData.postValue(state)
    }

    fun loadTracksHistory() {
        renderState(SearchState.History(tracksInteractor.createTrackArrayListFromJson()))
    }

    fun cleanHistory() {
        tracksInteractor.cleanHistory()
        renderState(SearchState.History(emptyList()))
    }

    fun searchTracksDebounce(currentUserRequest: String) {
        if (latestUserRequest == currentUserRequest) {
            return
        }
        this.latestUserRequest = currentUserRequest
        searchDebounceJob?.cancel()
        searchDebounceJob = viewModelScope.launch {
            delay(AUTO_SEARCHING_DELAY)
            searchTracks(currentUserRequest)
        }
    }


}