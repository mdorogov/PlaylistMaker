package com.practicum.playlistmaker.player.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.library.domain.db.PlaylistsDbInteractor
import com.practicum.playlistmaker.player.domain.api.TracksPlayerInteractor
import com.practicum.playlistmaker.player.ui.state.PlayerState
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.db.FavoriteTracksDbInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    application: Application,
    private val jsonTrack: String,
    private val tracksPlayerInteractor: TracksPlayerInteractor,
    private val searchHistoryInteractor: TracksInteractor,
    private val favoriteTracksDbInteractor: FavoriteTracksDbInteractor,
    private val playlistsDbInteractor: PlaylistsDbInteractor
) : AndroidViewModel(application) {

    companion object {
        const val PLAYBACK_TIME_UPDATE_DELAY = 300L
        const val WAIT_FOR_PREPARED_PLAYER_TIME_DELAY = 500L
    }

    private var screenPlayerStateLiveData = MutableLiveData<PlayerState>()
    private lateinit var trackModel: Track
    private var isTrackFavorite: Boolean = false

    private var timerJob: Job? = null
    private var playerStatusJob: Job? = null


    init {
        postPlayerContent()
    }

    private fun postPlayerContent() {
        trackModel = tracksPlayerInteractor.loadPlayerData(jsonTrack)
        viewModelScope.launch(Dispatchers.IO) {
            isTrackFavorite = favoriteTracksDbInteractor
                .isTrackFavorite(trackModel.trackId)
            if (!isTrackFavorite) {
                searchHistoryInteractor.addTrackToArray(trackModel)
            }
            screenPlayerStateLiveData.postValue(PlayerState.Content(trackModel, isTrackFavorite))
        }
    }

    private suspend fun isTrackFavoriteTEST(trackId: Int): Boolean {
        return favoriteTracksDbInteractor.isTrackFavorite(trackId)
    }

    private fun addTrackToFavorites() {
        trackModel.setTimeStamp()
        viewModelScope.launch(Dispatchers.IO) {
            favoriteTracksDbInteractor.insertFavTrack(trackModel)
        }
        screenPlayerStateLiveData.postValue(PlayerState.FavoriteTrackChanged(true))
    }

    private fun deleteTrackFromFavorites() {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteTracksDbInteractor.deleteFavTrack(trackModel)
        }
        screenPlayerStateLiveData.postValue(PlayerState.FavoriteTrackChanged(false))
    }


    fun getLoadingLiveData(): LiveData<PlayerState> = screenPlayerStateLiveData


    fun play() {
        playerStatusJob?.cancel()
        playerStatusJob = viewModelScope.launch {
            tracksPlayerInteractor.play(trackModel.previewUrl)
            delay(WAIT_FOR_PREPARED_PLAYER_TIME_DELAY)
            PlayerState.PlayTime(tracksPlayerInteractor.getCurrentPlayingPosition())
            startTimer()
        }
    }

    private suspend fun startTimer() {
        timerJob = viewModelScope.launch {
            while (tracksPlayerInteractor.getPlayingStatus()) {
                delay(PLAYBACK_TIME_UPDATE_DELAY)
                screenPlayerStateLiveData.postValue(
                    PlayerState.PlayTime(tracksPlayerInteractor.getCurrentPlayingPosition())
                )
                checkOnStop()
            }
        }
    }

    fun checkOnStop() {
        if (tracksPlayerInteractor.getIsSongPlayed()) {
            screenPlayerStateLiveData.postValue(PlayerState.PlayingStopped("00:00"))
        }
    }

    fun resume() {
        tracksPlayerInteractor.resume()
    }

    fun pause() {
        tracksPlayerInteractor.pause()
        timerJob?.cancel()
        screenPlayerStateLiveData.postValue(
            PlayerState.PlayTimePaused(tracksPlayerInteractor.getCurrentPlayingPosition())
        )
    }

    fun releasePlayer() {
        tracksPlayerInteractor.release()
        playerStatusJob?.cancel()
        timerJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        releasePlayer()
    }


    fun getScreenStateLiveData(): LiveData<PlayerState> = screenPlayerStateLiveData

    fun favoriteTrackIconIsPressed() {
        if (isTrackFavorite) {
            deleteTrackFromFavorites()
        } else {
            addTrackToFavorites()
        }
    }

    fun addTrackToPlaylist(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {

            val resultCode = playlistsDbInteractor.addTrackToPlaylist(playlistId, trackModel)
            val playlistName =
                playlistsDbInteractor.getPlaylistByPlaylistId(playlistId).playlistName

            if (resultCode == 1) {
                screenPlayerStateLiveData.postValue(
                    PlayerState.TrackIsAdded(
                        true,
                        trackModel.trackName,
                        playlistName
                    )
                )
            } else {
                screenPlayerStateLiveData.postValue(
                    PlayerState.TrackIsAdded(
                        false,
                        trackModel.trackName,
                        playlistName
                    )
                )
            }

        }
    }

    fun showBottomSheet() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsDbInteractor
                .getAllPlaylists()
                .collect { playlists ->
                    screenPlayerStateLiveData.postValue(PlayerState.PlaylistChoosing(playlists as ArrayList<Playlist>))
                }
        }

    }

    fun updatePlaylists() {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsDbInteractor
                .getAllPlaylists()
                .collect { playlists ->
                    screenPlayerStateLiveData.postValue(PlayerState.PlaylistUpdate(playlists as ArrayList<Playlist>))
                }
        }
    }
}

