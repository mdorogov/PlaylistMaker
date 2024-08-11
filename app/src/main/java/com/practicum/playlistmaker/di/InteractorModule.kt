package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.main.data.ThemeInteractorImpl
import com.practicum.playlistmaker.main.domain.ThemeInteractor
import com.practicum.playlistmaker.player.data.impl.TrackPlayerInteractorImpl
import com.practicum.playlistmaker.player.domain.api.TrackPlayerInteractor
import com.practicum.playlistmaker.player.domain.api.TracksPlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.TracksPlayerInteractorImpl
import com.practicum.playlistmaker.search.data.SearchHistory
import com.practicum.playlistmaker.search.domain.api.SearchHistoryInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.settings.data.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module


val interactorModule = module {

    single<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    single<TracksPlayerInteractor> {
        TracksPlayerInteractorImpl()
    }

    single<SearchHistoryInteractor> {
        SearchHistory(get())
    }



    single<ThemeInteractor> {
        ThemeInteractorImpl(get())
    }

    single<SharingInteractor>{
        SharingInteractorImpl(get())
    }

    factory<TrackPlayerInteractor>{
        TrackPlayerInteractorImpl(get())
    }
    single<SettingsInteractor>{
        SettingsInteractorImpl(get())
    }

}
