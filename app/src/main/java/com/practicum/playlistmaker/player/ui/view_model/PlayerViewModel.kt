package com.practicum.playlistmaker.player.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.player.domain.api.TrackPlayerRepository
import com.practicum.playlistmaker.player.domain.api.TracksPlayerInteractor
import com.practicum.playlistmaker.player.ui.state.PlayerState
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.TracksInteractor

class PlayerViewModel(
    application: Application,
    private val jsonTrack: String,
    private val tracksPlayerInteractor: TracksPlayerInteractor,
    //private val trackPlayer: TrackPlayerRepository,
    private val searchHistoryInteractor: TracksInteractor,
) : AndroidViewModel(application) {

    private var screenPlayerStateLiveData = MutableLiveData<PlayerState>()
    private lateinit var trackModel: Track


    init {
        trackModel = tracksPlayerInteractor.loadPlayerData(jsonTrack)
        searchHistoryInteractor.addTrackToArray(trackModel)
        screenPlayerStateLiveData.postValue(PlayerState.Content(trackModel))
    }


    fun getLoadingLiveData(): LiveData<PlayerState> = screenPlayerStateLiveData

    companion object {
    }

    fun play() {
        tracksPlayerInteractor.play(
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
        tracksPlayerInteractor.resume()
    }

    fun pause() {
        tracksPlayerInteractor.pause()
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