package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.domain.api.TracksPlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.TracksPlayerInteractorImpl
import com.practicum.playlistmaker.search.data.SearchHistory
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import org.koin.dsl.module



    val interactorModule = module {

        single<TracksInteractor>{
            TracksInteractorImpl(get())
        }

        single<TracksPlayerInteractor>{
            TracksPlayerInteractorImpl()
        }

        single<SearchHistoryInteractor>{
            SearchHistory(get())
        }
    }
