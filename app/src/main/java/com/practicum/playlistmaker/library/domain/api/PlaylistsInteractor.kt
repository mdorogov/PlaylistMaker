package com.practicum.playlistmaker.library.domain.api

import com.practicum.playlistmaker.library.data.models.Playlist

interface PlaylistsInteractor {
    fun getPlaylists(): ArrayList<Playlist>
}