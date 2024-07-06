package com.practicum.playlistmaker.search.ui.view_model

import android.app.Application
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.main.ui.App
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.ui.JSON_HISTORY_KEY
import com.practicum.playlistmaker.search.ui.state.SearchState


class SearchViewModel (application: Application) : AndroidViewModel(application) {
    private var loadingObserver: ((Boolean) -> Unit)? = null

    private var loadingLiveData = MutableLiveData(true)
    private var stateLiveData = MutableLiveData<SearchState>()

    private val handler = Handler(Looper.getMainLooper())

    private var tracksInteractor =  Creator.provideTracksInteractor(getApplication())
    private var searchHistoryHandler = tracksInteractor.getSearchHistory()

    private var latestUserRequest: String? = null


    companion object{

        private val SEARCH_REQUEST_TOKEN = Any()
        private const val AUTO_SEARCHING_DELAY = 2000L
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(this[APPLICATION_KEY] as App)
                }
            }
    }



    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }


    fun observeState(): LiveData<SearchState> = stateLiveData

    fun searchTracks(userRequest: String){
        if (userRequest.isNotEmpty()) {
            renderState(SearchState.Loading)

            tracksInteractor.searchTracks(userRequest, object : TracksInteractor.TracksConsumer {
                override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                    val songs = mutableListOf<Track>()
                    // Создание MoviesState
                    if (foundTracks != null) {
                        songs.clear()
                        songs.addAll(foundTracks)
                        renderState(SearchState.Content(foundTracks = songs))
                    }

                    if(errorMessage != null) {
                        renderState(SearchState.Error(errorMessage = "No Connection", userRequest = userRequest))
                    } else if(foundTracks.isNullOrEmpty()){
                        renderState(SearchState.Empty(message = "Not Found", userRequest = userRequest))
                    }

                }
            })
        }
    }

    fun setHistorySharedPrefListener(){
        var sharedListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == JSON_HISTORY_KEY) {
                    val history = sharedPreferences?.getString(JSON_HISTORY_KEY, null)
                    if (history != null) {
loadTracksHistory()
//                        historyRecycler.adapter = searchHistoryAdapter
//                        searchHistoryAdapter.notifyDataSetChanged()
                    }
                }
            }
        searchHistoryHandler.getSharedPrefs().registerOnSharedPreferenceChangeListener(sharedListener)
    }



    fun renderState(state: SearchState){
        stateLiveData.postValue(state)
    }

    fun loadTracksHistory() {
renderState(SearchState.History(searchHistoryHandler.createTrackArrayListFromJson()))
    }

    /*init {
        tracksInteractor.loadSomeData(
            onComplete = {
                loadingLiveData.postValue(false)
            }
        )
    }*/

    fun getLoadingLiveData(): LiveData<Boolean> = loadingLiveData

    var isLoading: Boolean = true
        private set(value) {
            field = value
            loadingObserver?.invoke(value)
        }

    fun addLoadingObserver(loadingObserver: ((Boolean) -> Unit)) {
        this.loadingObserver = loadingObserver
    }

    fun removeLoadingObserver(){
        this.loadingObserver = null
    }

    fun cleanHistory() {
       searchHistoryHandler.cleanHistory()
        renderState(SearchState.History(emptyList()))
    }

    fun searchTracksDebounce(currentUserRequest: String) {
        if (latestUserRequest == currentUserRequest){
            return
        }

        this.latestUserRequest = currentUserRequest
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchTracks(currentUserRequest) }

        val postTime = SystemClock.uptimeMillis() + AUTO_SEARCHING_DELAY
        handler.postAtTime(searchRunnable, SEARCH_REQUEST_TOKEN, postTime)
       // handler.postDelayed(searchRunnable, AUTO_SEARCHING_DELAY)


    }


}