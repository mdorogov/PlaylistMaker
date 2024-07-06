package com.practicum.playlistmaker.creator

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.player.data.api.PlayerRepository
import com.practicum.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.search.data.impl.SearchInteractorImpl
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.data.network.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.SearchInteractor
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
        return  TracksInteractorImpl(getTrackRepository(context))
    }

    private fun getPlayer(): PlayerRepositoryImpl {
        return PlayerRepositoryImpl()
    }

    fun providePlayer(): PlayerRepository {
        return getPlayer()

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

    fun provideSearchInteractor(): SearchInteractor {
        return getSearchInteractor()
    }

    private fun getSearchInteractor(): SearchInteractorImpl {
return SearchInteractorImpl()
    }

  /*  private fun getPlayerInteractor(): Player{

    }
    fun providePlayerInteractor(application: Application): Any {

    }*/

}