package com.practicum.playlistmaker.sharing.domain

import com.practicum.playlistmaker.search.data.models.Track

interface SharingInteractor {
fun shareApp()
fun showAgreement()
    fun sendEmail()
    fun sharePlaylist(playlistName: String, playlistDescription: String, playlistNumOfTracks: Int, tracks: List<Track>?)
}