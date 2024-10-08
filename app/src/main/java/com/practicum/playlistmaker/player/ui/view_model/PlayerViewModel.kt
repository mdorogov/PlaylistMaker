package com.practicum.playlistmaker.player.ui.view_model

import android.app.Application
import android.content.res.Resources
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Resource
import com.practicum.playlistmaker.player.domain.api.TrackPlayerRepository
import com.practicum.playlistmaker.player.domain.api.TracksPlayerInteractor
import com.practicum.playlistmaker.player.ui.state.PlayerState
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.db.FavoriteTracksDbInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.logging.Handler

class PlayerViewModel(
    application: Application,
    private val jsonTrack: String,
    private val tracksPlayerInteractor: TracksPlayerInteractor,
    private val searchHistoryInteractor: TracksInteractor,
    private val favoriteTracksDbInteractor: FavoriteTracksDbInteractor
) : AndroidViewModel(application) {

    companion object{
        const val PLAYBACK_TIME_UPDATE_DELAY = 300L
        const val WAIT_FOR_PREPARED_PLAYER_TIME_DELAY = 500L
    }

    private var screenPlayerStateLiveData = MutableLiveData<PlayerState>()
    private lateinit var trackModel: Track

    private var timerJob: Job? = null
    private var playerStatusJob: Job? = null


    init {
        trackModel = tracksPlayerInteractor.loadPlayerData(jsonTrack)
        searchHistoryInteractor.addTrackToArray(trackModel)
        screenPlayerStateLiveData.postValue(PlayerState.Content(trackModel))
    }

    private suspend fun isTrackFavorite(trackId: Int) : Boolean{
return favoriteTracksDbInteractor.isTrackFavorite(trackId)
    }

    private suspend fun addTrackToFavorites(track: Track) {
        favoriteTracksDbInteractor.insertFavTrack(track)
    }

    private suspend fun deleteTrackFromFavorites(track: Track) {
        favoriteTracksDbInteractor.deleteFavTrack(track)
    }


    fun getLoadingLiveData(): LiveData<PlayerState> = screenPlayerStateLiveData


    fun play(){
        tracksPlayerInteractor.play(trackModel.previewUrl)
        playerStatusJob?.cancel()
       playerStatusJob = viewModelScope.launch {
           delay(WAIT_FOR_PREPARED_PLAYER_TIME_DELAY)
           PlayerState.PlayTime(tracksPlayerInteractor.getCurrentPlayingPosition())
           startTimer()
       }
    }

    private suspend fun startTimer(){
        timerJob = viewModelScope.launch {
            while(tracksPlayerInteractor.getPlayingStatus()) {
                delay(PLAYBACK_TIME_UPDATE_DELAY)
                screenPlayerStateLiveData.postValue(
                    PlayerState.PlayTime(tracksPlayerInteractor.getCurrentPlayingPosition())
                )
                checkOnStop()
            }
            }

        /*playerStatusJob?.cancel()
        playerStatusJob = viewModelScope.launch {
            checkOnStop()
        }*/

    }

    fun checkOnStop(){
        if(tracksPlayerInteractor.getIsSongPlayed()) {
            screenPlayerStateLiveData.postValue(PlayerState.PlayingStopped("00:00"))
        }
    }

    fun resume() {
        tracksPlayerInteractor.resume()
    }

    fun pause() {
        tracksPlayerInteractor.pause()
        timerJob?.cancel()
        screenPlayerStateLiveData.postValue(PlayerState.
        PlayTimePaused(tracksPlayerInteractor.getCurrentPlayingPosition()))
    }

    fun releasePlayer() {
        tracksPlayerInteractor.release()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun getScreenStateLiveData(): LiveData<PlayerState> = screenPlayerStateLiveData

}