package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.search.data.network.TrackRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.TrackRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import org.koin.dsl.module


val repositoryModule = module {
    single<TrackRepository> {
        TrackRepositoryImpl(get(), get())
    }

    single<ExternalNavigator> {
        ExternalNavigator(get())
    }


}
