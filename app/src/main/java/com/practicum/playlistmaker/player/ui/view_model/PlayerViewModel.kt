package com.practicum.playlistmaker.player.ui.view_model

import android.app.Application
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.api.TrackPlayerRepository
import com.practicum.playlistmaker.player.domain.api.TracksPlayerInteractor
import com.practicum.playlistmaker.player.ui.state.PlayerState
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.logging.Handler

class PlayerViewModel(
    application: Application,
    private val jsonTrack: String,
    private val tracksPlayerInteractor: TracksPlayerInteractor,
    //private val trackPlayer: TrackPlayerRepository,
    private val searchHistoryInteractor: TracksInteractor,
) : AndroidViewModel(application) {

    companion object{
        const val PLAYBACK_TIME_UPDATE_DELAY = 500L
    }

    private var screenPlayerStateLiveData = MutableLiveData<PlayerState>()
    private lateinit var trackModel: Track

    private var timerJob: Job? = null


    init {
        trackModel = tracksPlayerInteractor.loadPlayerData(jsonTrack)
        searchHistoryInteractor.addTrackToArray(trackModel)
        screenPlayerStateLiveData.postValue(PlayerState.Content(trackModel))
    }


    fun getLoadingLiveData(): LiveData<PlayerState> = screenPlayerStateLiveData

    fun play1() {
     /*   tracksPlayerInteractor.play(
            trackModel.previewUrl,
            statusObserver = object : TrackPlayerRepository.StatusObserver {
                override fun onProgress(progress: String) {
                    screenPlayerStateLiveData.value = PlayerState.PlayTime(
                        progress = progress,
                        isPlaying = true
                    )
                }

                override fun onStop() {
                    screenPlayerStateLiveData.value = PlayerState.PlayTime(
                        progress = trackModel.getPlayerTrackTime(),
                        isPlaying = false
                    )
                }

                override fun onPlay() {
                    screenPlayerStateLiveData.value = PlayerState.PlayTime(
                        progress = "00:00",
                        isPlaying = true
                    )
                }

                override fun onPause(progress: String) {
                    screenPlayerStateLiveData.value = PlayerState.PlayTime(
                        progress = progress,
                        isPlaying = false
                    )
                }
            },
        )*/
    }

    fun play(){
        tracksPlayerInteractor.play(trackModel.previewUrl)

        android.os.Handler(Looper.getMainLooper()).postDelayed({
            if (tracksPlayerInteractor.getPlayingStatus()){
                screenPlayerStateLiveData.postValue(PlayerState.
                PlayTime(tracksPlayerInteractor.getCurrentPlayingPosition()))
                startTimer()
            }
        }, 400)
    }

    private fun startTimer(){
        var wtf = tracksPlayerInteractor.getPlayingStatus()

        timerJob = viewModelScope.launch {
            while(tracksPlayerInteractor.getPlayingStatus()) {
                delay(PLAYBACK_TIME_UPDATE_DELAY)
                screenPlayerStateLiveData.postValue(
                    PlayerState.PlayTime(tracksPlayerInteractor.getCurrentPlayingPosition())
                )
            }
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