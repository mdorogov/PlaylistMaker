package com.practicum.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.library.data.db.PlaylistsDatabase
import com.practicum.playlistmaker.library.data.db.SavedTracksDatabase
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.db.FavoriteTracksDatabase
import com.practicum.playlistmaker.search.data.network.ItunesApi
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val dataModule = module {
    single<ItunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    single {
        Room.databaseBuilder(
            androidContext(), FavoriteTracksDatabase::class.java,
            "favtracksdatabase.db"
        ).build()
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            PlaylistsDatabase::class.java,
            "playlistsdatabase.db"
        ).build()
    }

    single {
        Room.databaseBuilder(
            androidContext(),
            SavedTracksDatabase::class.java,
            "savedtracksdatabase.db"
        ).build()
    }

    factory { Gson() }



    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<MediaPlayer> {
        MediaPlayer()
    }
}
