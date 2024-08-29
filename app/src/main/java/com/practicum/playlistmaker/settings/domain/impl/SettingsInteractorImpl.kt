package com.practicum.playlistmaker.settings.domain.impl

import android.content.SharedPreferences
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

class SettingsInteractorImpl(var settingsRepository: SettingsRepository) : SettingsInteractor {

    override fun getThemeSettings(): ThemeSettings {
        return  settingsRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: Boolean) {
        settingsRepository.updateThemeSetting(settings)

    }
}