package com.practicum.playlistmaker.library.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.api.FavoriteTracksInteractor
import com.practicum.playlistmaker.library.domain.db.PlaylistsDbInteractor
import com.practicum.playlistmaker.library.ui.state.FavoriteTracksState
import com.practicum.playlistmaker.library.ui.state.PlaylistsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsInteractor: PlaylistsDbInteractor
) : ViewModel() {
    private var stateLiveData = MutableLiveData<PlaylistsState>()

    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    fun loadData() {

        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.getAllPlaylists()
                .collect { playlists ->
                    if (!playlists.isNullOrEmpty()) {
                        stateLiveData.postValue(PlaylistsState.Content(playlists))
                    } else {
                        stateLiveData.postValue(PlaylistsState.ContentNotFound(1))
                    }

                }
        }

    }
}