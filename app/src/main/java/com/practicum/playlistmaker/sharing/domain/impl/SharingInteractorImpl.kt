package com.practicum.playlistmaker.sharing.domain.impl

import android.provider.Settings.Global.getString
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.library.mapper.WordFormConverter
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }


    override fun showAgreement() {
        externalNavigator.openLink(getTermsLink())
    }


    override fun sendEmail() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    override fun sharePlaylist(playlistName: String, playlistDescription: String, playlistNumOfTracks: Int, tracks: List<Track>?) {

        externalNavigator.sharePlaylist(getPlaylistData(playlistName, playlistDescription, playlistNumOfTracks, tracks))
    }


    private fun getSupportEmailData(): EmailData {
        return EmailData(
            arrayOf("dorogovmax@yandex.com"),
            externalNavigator.getString(R.string.sharing_email_subject_text),
            externalNavigator.getString(R.string.sharing_email_desc_text)
        )
    }

    private fun getShareAppLink(): String {
        return externalNavigator.getString(R.string.share_app_link)
    }

    private fun getTermsLink(): String {
        return externalNavigator.getString(R.string.agreement_link)
    }

    private fun getPlaylistData(playlistName: String, playlistDescription: String, playlistNumOfTracks: Int, tracks: List<Track>?) : String {

        val tracksData = getTracksData(tracks)
        return "$playlistName \n$playlistDescription \n" + WordFormConverter.getTrackWordForm(playlistNumOfTracks) + "\n$tracksData"
    }

    private fun getTracksData(tracks: List<Track>?): String {
        var string = ""
        if (!tracks.isNullOrEmpty()) {
            for (track in tracks) {
                val numOfTrack = tracks.indexOf(track) + 1
                val trackString = track.convertTrackToString()
                string += "$numOfTrack $trackString\n"
            }
        }
        return string
    }
}