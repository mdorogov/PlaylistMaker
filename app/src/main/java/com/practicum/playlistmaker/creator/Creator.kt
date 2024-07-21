package com.practicum.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import android.provider.MediaStore.Audio.Media
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.player.data.impl.TrackPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.TrackPlayerRepository
import com.practicum.playlistmaker.player.domain.api.TracksPlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.TracksPlayerInteractorImpl
import com.practicum.playlistmaker.search.data.SearchHistory
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.network.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
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
object Creator {

    private lateinit var application: Application
    fun initApplication(app: Application){
        application = app
    }


    private fun getTrackRepository(): TrackRepository {
        val prefs= application.applicationContext.getSharedPreferences(SEARCH_TRACK_HISTORY, MODE_PRIVATE)
        return TrackRepositoryImpl(RetrofitNetworkClient(), prefs)
    }

    fun provideTracksInteractor(): TracksInteractor {
        return  getTracksInteractor()
    }

    private fun getTracksInteractor(): TracksInteractor {
        return TracksInteractorImpl(getTrackRepository())
    }

    fun provideTrackPlayer(): TrackPlayerRepository{
        return getTrackPlayerRepository()
    }

    private fun getTrackPlayerRepository(): TrackPlayerRepository {
        return TrackPlayerRepositoryImpl(MediaPlayer())
    }

    private fun getSharingInteractor(): SharingInteractorImpl {
        return SharingInteractorImpl(ExternalNavigator(application.applicationContext))
    }

    fun provideSharingInteractor(): SharingInteractor {
       return getSharingInteractor()
    }

    private fun getSettingsRepo(): SettingsRepositoryImpl {
        return SettingsRepositoryImpl(application.applicationContext)
    }

    fun provideSettingsRepo(): SettingsRepository{
        return getSettingsRepo()
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

    fun provideSearchHistoryInteractor(): SearchHistoryInteractor {
return getSearchHistoryInteractor()
    }

    private fun getSearchHistoryInteractor(): SearchHistoryInteractor {
        val prefs= application.applicationContext.getSharedPreferences(SEARCH_TRACK_HISTORY, MODE_PRIVATE)
return SearchHistory(prefs)
    }


}