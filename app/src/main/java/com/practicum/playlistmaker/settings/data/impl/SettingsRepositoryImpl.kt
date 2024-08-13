package com.practicum.playlistmaker.settings.data.impl

import android.content.SharedPreferences
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings
import com.practicum.playlistmaker.settings.ui.activity.NIGHT_THEME_CHECKED

class SettingsRepositoryImpl(var sharedPrefs: SharedPreferences): SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        return  ThemeSettings(sharedPrefs.getString(NIGHT_THEME_CHECKED, "false").toBoolean())
    }

    override fun updateThemeSetting(settings: Boolean) {
        sharedPrefs.edit().putString(NIGHT_THEME_CHECKED, settings.toString()).apply()
    }
}