package com.practicum.playlistmaker.settings.data.impl

import android.app.Application
import android.content.Context
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings
import com.practicum.playlistmaker.settings.ui.activity.NIGHT_THEME_CHECKED
import com.practicum.playlistmaker.settings.ui.activity.PLAYLIST_MAKER_PREFERENCES

class SettingsRepositoryImpl(context: Context) : SettingsRepository {
    val sharedPrefs =
        context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Application.MODE_PRIVATE)

    override fun getThemeSettings(): ThemeSettings {
        return ThemeSettings(sharedPrefs.getString(NIGHT_THEME_CHECKED, "false").toBoolean())
    }

    override fun updateThemeSetting(settings: Boolean) {
        sharedPrefs.edit().putString(NIGHT_THEME_CHECKED, settings.toString()).apply()
    }
}