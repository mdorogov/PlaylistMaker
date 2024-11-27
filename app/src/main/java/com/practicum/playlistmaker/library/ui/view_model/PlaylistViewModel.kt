package com.practicum.playlistmaker.library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.library.domain.db.PlaylistsDbInteractor
import com.practicum.playlistmaker.library.ui.state.PlaylistState
import com.practicum.playlistmaker.search.mapper.MillisConverter
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val playlistsDbInteractor: PlaylistsDbInteractor,
    private val sharingInteractor: SharingInteractor
) : ViewModel() {

    private var stateLiveData = MutableLiveData<PlaylistState>()

    fun observeState(): LiveData<PlaylistState> = stateLiveData

    fun loadData(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = playlistsDbInteractor.getPlaylistByPlaylistId(playlistId)
            val pairResult = playlistsDbInteractor.getSavedTracksByPlaylistID(playlistId)
            stateLiveData.postValue(
                PlaylistState.Content(
                    playlist,
                    pairResult.first,
                    pairResult.second
                )
            )
        }

    }

    fun deleteTrackFromPlaylist(trackId: Int, playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsDbInteractor.deleteTrackFromPlaylist(playlistId, trackId)
            stateLiveData.postValue(PlaylistState.TrackIsDeleted(true))
            loadData(playlistId)
        }
    }


    fun sharePlaylist(playlistId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val playlist = playlistsDbInteractor.getPlaylistByPlaylistId(playlistId)
            val pairResult = playlistsDbInteractor.getSavedTracksByPlaylistID(playlistId)
            sharingInteractor.sharePlaylist(
                playlist.playlistName,
                playlist.description,
                playlist.numOfTracks,
                pairResult.first
            )
        }
    }

    fun deletePlaylist(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsDbInteractor.deletePlaylist(id)
            stateLiveData.postValue(PlaylistState.PlaylistIsDeleted(true))
        }
    }
}