package com.practicum.playlistmaker.main.data

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.main.domain.ThemeInteractor
const val NIGHT_THEME_CHECKED = "key_for_theme_switcher"

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