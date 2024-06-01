package com.practicum.playlistmaker.creator

import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.data.api.PlayerRepository
import com.practicum.playlistmaker.data.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.network.TrackRepositoryImpl
import com.practicum.playlistmaker.data.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.TrackRepository
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.impl.TracksInteractorImpl
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

    fun providePlayer(): PlayerRepository{
        return getPlayer()

    }
}