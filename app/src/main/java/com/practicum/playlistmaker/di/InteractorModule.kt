package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.library.domain.api.FavoriteTracksInteractor
import com.practicum.playlistmaker.library.domain.api.PlaylistsInteractor
import com.practicum.playlistmaker.library.domain.impl.FavoriteTracksInteractorImpl
import com.practicum.playlistmaker.library.domain.impl.PlaylistsInteractorImpl
import com.practicum.playlistmaker.main.data.ThemeInteractorImpl
import com.practicum.playlistmaker.main.domain.ThemeInteractor
import com.practicum.playlistmaker.player.data.impl.TrackPlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.TrackPlayerRepository
import com.practicum.playlistmaker.player.domain.api.TracksPlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.TracksPlayerInteractorImpl
import com.practicum.playlistmaker.search.data.impl.FavoriteTracksDbInteractorImpl
import com.practicum.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.network.TrackSearchInteractorImpl
import com.practicum.playlistmaker.search.domain.api.SearchHistoryRepository
import com.practicum.playlistmaker.search.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.db.FavoriteTracksDbInteractor
import com.practicum.playlistmaker.search.domain.db.FavoriteTracksRepository
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.core.scope.get
import org.koin.dsl.module


val interactorModule = module {

    factory<TracksInteractor> {
        TracksInteractorImpl(get(), get())
    }

    factory<TracksPlayerInteractor> {
        TracksPlayerInteractorImpl(get())
    }
    factory<ThemeInteractor> {
        ThemeInteractorImpl(get())
    }

    factory<SharingInteractor>{
        SharingInteractorImpl(get())
    }

    factory<SettingsInteractor>{
        SettingsInteractorImpl(get())
    }

    factory<TrackSearchInteractor> {
        TrackSearchInteractorImpl(get())
    }

    factory<FavoriteTracksInteractor>{
        FavoriteTracksInteractorImpl()
    }

    factory<PlaylistsInteractor>{
        PlaylistsInteractorImpl()
    }

    single<FavoriteTracksDbInteractor> {
        FavoriteTracksDbInteractorImpl(get())
    }

}
