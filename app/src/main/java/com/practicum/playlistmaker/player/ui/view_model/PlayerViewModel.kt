package com.practicum.playlistmaker.player.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.main.ui.App
import com.practicum.playlistmaker.player.domain.api.TrackPlayerRepository
import com.practicum.playlistmaker.player.domain.api.TracksPlayerInteractor
import com.practicum.playlistmaker.player.ui.state.PlayerState
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor

class PlayerViewModel(
    application: Application,
    private val jsonTrack: String,
    private val tracksPlayerInteractor: TracksPlayerInteractor,
    private val trackPlayer: TrackPlayerRepository,
    private val searchHistoryInteractor: SearchHistoryInteractor
) : AndroidViewModel(application) {

    private var screenPlayerStateLiveData = MutableLiveData<PlayerState>()
    // private val searchHistoryInteractor = Creator.provideSearchHistoryInteractor()
    private lateinit var trackModel: Track


    init {
        trackModel = tracksPlayerInteractor.loadPlayerData(jsonTrack)
        searchHistoryInteractor.addTrackToArray(trackModel)
        screenPlayerStateLiveData.postValue(PlayerState.Content(trackModel))
    }


    fun getLoadingLiveData(): LiveData<PlayerState> = screenPlayerStateLiveData

    companion object {
      /*  fun getViewModelFactory(jsonTrack: String): ViewModelProvider.Factory = viewModelFactory {
            initializer {

                val interactor = Creator.provideTracksPlayerInteractor()
                val trackPlayer = Creator.provideTrackPlayer()

                PlayerViewModel(this[APPLICATION_KEY] as App, jsonTrack, interactor, trackPlayer)
            }
        }*/
    }

    fun play() {
        trackPlayer.play(
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
        )
    }

    fun resume() {
        trackPlayer.resume()
    }

    fun pause() {
        trackPlayer.pause()
    }

    fun releasePlayer() {
        trackPlayer.release()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }

    fun getScreenStateLiveData(): LiveData<PlayerState> = screenPlayerStateLiveData

}