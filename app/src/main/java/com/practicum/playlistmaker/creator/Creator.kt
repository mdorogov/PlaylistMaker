package com.practicum.playlistmaker.creator

import android.content.Context
import android.media.MediaPlayer
import android.provider.MediaStore.Audio.Media
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.player.data.impl.TrackPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.TrackPlayerRepository
import com.practicum.playlistmaker.player.domain.api.TracksPlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.TracksPlayerInteractorImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.network.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl

const val SEARCH_TRACK_HISTORY = "search_track_history"
const val JSON_HISTORY_KEY = "key_for_json_history"
object Creator : AppCompatActivity() {

    private fun getTrackRepository(context: Context): TrackRepository {
        val prefs= context.getSharedPreferences(SEARCH_TRACK_HISTORY, MODE_PRIVATE)
        return TrackRepositoryImpl(RetrofitNetworkClient(), prefs)
    }

    fun provideTracksInteractor(context: Context): TracksInteractor {
        return  getTracksInteractor(context)
    }

    private fun getTracksInteractor(context: Context): TracksInteractor {
        return TracksInteractorImpl(getTrackRepository(context))
    }

    fun provideTrackPlayer(): TrackPlayerRepository{
        return getTrackPlayerRepository()
    }

    private fun getTrackPlayerRepository(): TrackPlayerRepository {
        return TrackPlayerRepositoryImpl()
    }

    private fun getSharingInteractor(context: Context): SharingInteractorImpl {
        return SharingInteractorImpl(ExternalNavigator(context))
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
       return getSharingInteractor(context)
    }

    private fun getSettingsRepo(context: Context): SettingsRepositoryImpl {
        return SettingsRepositoryImpl(context)
    }

    fun provideSettingsRepo(context: Context): SettingsRepository{
        return getSettingsRepo(context)
    }

    fun provideTracksPlayerInteractor(): TracksPlayerInteractor {
        return getTracksPlayerInteractor()
    }

    private fun getTracksPlayerInteractor(): TracksPlayerInteractor {
        return TracksPlayerInteractorImpl()

    }

    fun provideMediaPlayer(): MediaPlayer {
return getMediaPlayer()
    }

    private fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer()
    }


}