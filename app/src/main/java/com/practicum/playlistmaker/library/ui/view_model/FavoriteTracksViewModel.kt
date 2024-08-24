package com.practicum.playlistmaker.library.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.library.domain.api.FavoriteTracksInteractor
import com.practicum.playlistmaker.library.ui.state.FavoriteTracksState
import com.practicum.playlistmaker.search.domain.api.TracksInteractor

class FavoriteTracksViewModel(application: Application,
                              private val favoriteTracksInteractor: FavoriteTracksInteractor
) : AndroidViewModel(application)  {
    private var stateLiveData = MutableLiveData<FavoriteTracksState>()
    fun observeState(): LiveData<FavoriteTracksState> = stateLiveData
}