package com.practicum.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.db.PlaylistsDbInteractor
import com.practicum.playlistmaker.library.ui.state.PlaylistState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistsDbInteractor: PlaylistsDbInteractor): ViewModel() {

    private var stateLiveData = MutableLiveData<PlaylistState>()

    fun observeState(): LiveData<PlaylistState> = stateLiveData

    fun loadData(playlistId: Long){
        viewModelScope.launch(Dispatchers.IO) {
            val playlist =  playlistsDbInteractor.getPlaylistByPlaylistId(playlistId)

            playlistsDbInteractor.getSavedTracksByPlaylistID(playlistId)
                stateLiveData.postValue(PlaylistState.Content(playlist))
        }

    }
}