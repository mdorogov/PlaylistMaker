package com.practicum.playlistmaker.library.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.api.FavoriteTracksInteractor
import com.practicum.playlistmaker.library.ui.state.FavoriteTracksState
import com.practicum.playlistmaker.library.ui.state.PlaylistsState
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.db.FavoriteTracksDbInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteTracksViewModel(private val favoriteTracksInteractor: FavoriteTracksDbInteractor
) : ViewModel()  {
    private var stateLiveData = MutableLiveData<FavoriteTracksState>()
    fun observeState(): LiveData<FavoriteTracksState> = stateLiveData

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            favoriteTracksInteractor
                .favoriteTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
        stateLiveData.postValue(FavoriteTracksState.ContentNotFound(1))
    }

    private fun processResult(tracks: List<Track>) {
if (tracks.isEmpty()){
    stateLiveData.postValue(FavoriteTracksState.ContentNotFound(1))
} else {
    stateLiveData.postValue(FavoriteTracksState.Content(tracks))
}
    }
}