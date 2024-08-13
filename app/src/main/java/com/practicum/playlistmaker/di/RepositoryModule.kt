package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.impl.TrackPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.TrackPlayerRepository
import com.practicum.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.network.TrackSearchInteractorImpl
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import org.koin.dsl.module


val repositoryModule = module {

    single<ExternalNavigator> {
        ExternalNavigator(get())
    }

    single<TrackPlayerRepository>{
        TrackPlayerRepositoryImpl(get())
    }

    factory<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(get())
    }

    factory<SettingsRepository>{
        SettingsRepositoryImpl(get())
    }

}
