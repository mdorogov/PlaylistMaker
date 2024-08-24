package com.practicum.playlistmaker.library.domain.impl

import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.library.domain.api.PlaylistsInteractor

class PlaylistsInteractorImpl: PlaylistsInteractor {
    override fun getPlaylists(): ArrayList<Playlist> {
      return ArrayList()
    }
}