package com.practicum.playlistmaker.main.ui

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.player.data.api.PlayerRepository
import com.practicum.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.data.impl.TrackPlayerImpl
import com.practicum.playlistmaker.player.data.impl.TracksPlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.api.TrackPlayer
import com.practicum.playlistmaker.player.domain.api.TracksPlayerInteractor
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.network.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.settings.ui.activity.NIGHT_THEME_CHECKED
import com.practicum.playlistmaker.settings.ui.activity.PLAYLIST_MAKER_PREFERENCES

class App : Application() {
    var darkTheme = false
    private lateinit var sharedPrefs: SharedPreferences
    override fun onCreate(){
        super.onCreate()
sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val preference = sharedPrefs.getString(NIGHT_THEME_CHECKED, "false").toBoolean()
switchTheme(preference)
    }

    fun switchTheme(darkThemeEnabled: Boolean){
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun provideTracksInteractor(): TracksInteractorImpl{
        return TracksInteractorImpl(TrackRepositoryImpl(RetrofitNetworkClient(),sharedPrefs))
    }

    fun provideTrackPlayer(): TrackPlayer{
        return getTracksPlayerImpl()
    }

    private fun getTracksPlayerImpl(): TrackPlayer {
return TrackPlayerImpl()

    }


    fun provideTracksPlayerInteractor(): TracksPlayerInteractor{
        return getTracksPlayerInteractor()
    }

    private fun getTracksPlayerInteractor(): TracksPlayerInteractor {
return TracksPlayerInteractorImpl()

    }

    private fun getTracksPlayerRepository(): PlayerRepositoryImpl {
return PlayerRepositoryImpl()
    }

    fun providePlayerRepository(): PlayerRepository {
return getPlayerRepositoryImpl()
    }

    private fun getPlayerRepositoryImpl(): PlayerRepository {
return PlayerRepositoryImpl()
    }
}