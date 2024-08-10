package com.practicum.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.NetworkClient
import com.practicum.playlistmaker.search.data.SearchHistory
import com.practicum.playlistmaker.search.data.network.ItunesApi
import com.practicum.playlistmaker.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


    val dataModule = module {
single<ItunesApi>{
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

        factory { Gson() }

        single<SearchHistoryInteractor> {
            SearchHistory(get())
        }

        single<NetworkClient>{
            RetrofitNetworkClient(get())
        }

        single<MediaPlayer>{
            MediaPlayer()
        }
    }
