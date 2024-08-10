package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.impl.TrackPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.TrackPlayerRepository
import com.practicum.playlistmaker.search.data.SearchHistory
import com.practicum.playlistmaker.search.data.network.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import org.koin.dsl.module


    val repositoryModule = module {
        single<TrackRepository>{
            TrackRepositoryImpl(get(), get())
        }

        single<SearchHistoryInteractor>{
            SearchHistory(get())
        }

        single<TrackPlayerRepository>{
            TrackPlayerRepositoryImpl(get())
        }
    }
