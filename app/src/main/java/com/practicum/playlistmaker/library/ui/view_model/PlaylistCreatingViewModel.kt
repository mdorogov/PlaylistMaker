package com.practicum.playlistmaker.library.ui.view_model

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.db.PlaylistsDbInteractor
import com.practicum.playlistmaker.library.ui.state.PlaylistCreatingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class PlaylistCreatingViewModel(
    private val playlistInteractor: PlaylistsDbInteractor
) : ViewModel() {

    private lateinit var artwork: Uri

    private var stateLiveData = MutableLiveData<PlaylistCreatingState>()
    fun observeState(): LiveData<PlaylistCreatingState> = stateLiveData

    fun createPlaylist(artworkUri: String, playlistName: String, playlistDescription: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.createPlaylist(artworkUri, playlistName, playlistDescription)
            stateLiveData.postValue(PlaylistCreatingState.PlaylistCreated(playlistName))
        }
    }


}