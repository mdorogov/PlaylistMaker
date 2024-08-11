package com.practicum.playlistmaker.settings.ui.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.ui.state.SettingsState
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    application: Application,
    private val sharingInteractor: SharingInteractor,
    private val settingsRepository: SettingsInteractor,
) : AndroidViewModel(application) {


    private val settingState = MutableLiveData<SettingsState>()


    init {
        updateThemeSwitcher()
    }

    fun getSettingsState(): LiveData<SettingsState> = settingState

    private fun updateThemeSwitcher() {
        settingState.postValue(SettingsState(settingsRepository.getThemeSettings().isDarkModeON))
    }

    fun saveThemePreference(checked: Boolean) {
        settingsRepository.updateThemeSetting(checked)
        settingState.postValue(SettingsState(checked))
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun sendEmail() {
        sharingInteractor.sendEmail()
    }

    fun showAgreement() {
        sharingInteractor.showAgreement()
    }

}