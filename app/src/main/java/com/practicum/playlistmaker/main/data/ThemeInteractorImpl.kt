package com.practicum.playlistmaker.main.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.main.domain.ThemeInteractor
import com.practicum.playlistmaker.settings.ui.activity.NIGHT_THEME_CHECKED

class ThemeInteractorImpl(var sharedPrefs: SharedPreferences) : ThemeInteractor {

    override fun switchTheme() {
        val darkThemeEnabled = sharedPrefs.getString(NIGHT_THEME_CHECKED, "false").toBoolean()
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

}