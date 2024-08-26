package com.practicum.playlistmaker.library.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.library.domain.api.FavoriteTracksInteractor
import com.practicum.playlistmaker.library.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.library.ui.state.FavoriteTracksState
import com.practicum.playlistmaker.library.ui.state.PlaylistsState

class PlaylistsViewModel(private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {
    private var stateLiveData = MutableLiveData<PlaylistsState>()

    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    fun loadData() {
        stateLiveData.postValue(PlaylistsState.ContentNotFound(1))
    }
}