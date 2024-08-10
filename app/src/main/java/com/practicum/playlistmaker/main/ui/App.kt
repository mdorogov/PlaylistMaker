package com.practicum.playlistmaker.main.ui

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.di.dataModule
import com.practicum.playlistmaker.di.interactorModule
import com.practicum.playlistmaker.di.repositoryModule
import com.practicum.playlistmaker.di.viewModelModule
import com.practicum.playlistmaker.settings.ui.activity.NIGHT_THEME_CHECKED
import com.practicum.playlistmaker.settings.ui.activity.PLAYLIST_MAKER_PREFERENCES
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    var darkTheme = false
        private lateinit var sharedPrefs: SharedPreferences
    override fun onCreate(){
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, interactorModule, repositoryModule,viewModelModule)
        }
        //dataModule, interactorModule, repositoryModule,
        //                viewModelModule


      //  Creator.initApplication(this)
sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        val preference = sharedPrefs.getString(NIGHT_THEME_CHECKED, "false").toBoolean()
switchTheme(preference)



    }

    fun switchTheme(darkThemeEnabled: Boolean){
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled){
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}