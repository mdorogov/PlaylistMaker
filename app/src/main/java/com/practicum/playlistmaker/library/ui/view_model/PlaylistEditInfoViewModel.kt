package com.practicum.playlistmaker.library.ui.view_model

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.domain.db.PlaylistsDbInteractor
import com.practicum.playlistmaker.library.ui.state.PlaylistCreatingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistEditInfoViewModel(private val playlistInteractor: PlaylistsDbInteractor) :
    PlaylistCreatingViewModel(
        playlistInteractor
    ) {


    fun loadPlaylistToEdit(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = playlistInteractor.getPlaylistByPlaylistId(playlistId)
            stateLiveData.postValue(PlaylistCreatingState.PlaylistEdit(playlist))
        }
    }

    fun updatePlaylist(
        playlistId: Long,
        playlistArtwork: Uri?,
        playlistName: String,
        playlistDesctription: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.updatePlaylist(
                playlistId,
                playlistArtwork,
                playlistName,
                playlistDesctription
            )
        }
    }

}