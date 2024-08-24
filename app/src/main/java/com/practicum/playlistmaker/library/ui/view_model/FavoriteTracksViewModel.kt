package com.practicum.playlistmaker.library.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.library.domain.api.FavoriteTracksInteractor
import com.practicum.playlistmaker.library.ui.state.FavoriteTracksState
import com.practicum.playlistmaker.library.ui.state.PlaylistsState
import com.practicum.playlistmaker.search.domain.api.TracksInteractor

class FavoriteTracksViewModel(private val favoriteTracksInteractor: FavoriteTracksInteractor
) : ViewModel()  {
    private var stateLiveData = MutableLiveData<FavoriteTracksState>()
    fun observeState(): LiveData<FavoriteTracksState> = stateLiveData

    fun loadData() {
        stateLiveData.postValue(FavoriteTracksState.ContentNotFound(1))
    }
}