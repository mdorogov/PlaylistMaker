package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.library.domain.api.FavoriteTracksInteractor
import com.practicum.playlistmaker.library.domain.db.PlaylistsDbInteractor
import com.practicum.playlistmaker.library.domain.impl.FavoriteTracksInteractorImpl
import com.practicum.playlistmaker.library.domain.impl.PlaylistsDbInteractorImpl
import com.practicum.playlistmaker.main.data.ThemeInteractorImpl
import com.practicum.playlistmaker.main.domain.ThemeInteractor
import com.practicum.playlistmaker.player.domain.api.TracksPlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.TracksPlayerInteractorImpl
import com.practicum.playlistmaker.search.data.impl.FavoriteTracksDbInteractorImpl
import com.practicum.playlistmaker.search.data.network.TrackSearchInteractorImpl
import com.practicum.playlistmaker.search.domain.api.TrackSearchInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.domain.db.FavoriteTracksDbInteractor
import com.practicum.playlistmaker.search.domain.impl.TracksInteractorImpl
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
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

    factory<SharingInteractor> {
        SharingInteractorImpl(get())
    }

    factory<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<TrackSearchInteractor> {
        TrackSearchInteractorImpl(get())
    }

    factory<FavoriteTracksInteractor> {
        FavoriteTracksInteractorImpl()
    }

    single<PlaylistsDbInteractor> {
        PlaylistsDbInteractorImpl(get())
    }

    single<FavoriteTracksDbInteractor> {
        FavoriteTracksDbInteractorImpl(get())
    }

}
