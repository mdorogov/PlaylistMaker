package com.practicum.playlistmaker.settings.ui.view_model

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.main.ui.App
import com.practicum.playlistmaker.settings.domain.SettingsRepository
import com.practicum.playlistmaker.settings.ui.state.SettingsState
import com.practicum.playlistmaker.sharing.domain.SharingInteractor

class SettingsViewModel(
    application: Application
) : AndroidViewModel(application) {


    private val sharingInteractor: SharingInteractor = Creator.provideSharingInteractor(getApplication<Application>())
    private val settingsRepository: SettingsRepository = Creator.provideSettingsRepo(getApplication<Application>())

    companion object{
        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SettingsViewModel(this[APPLICATION_KEY] as Application)
                }
            }
    }

    private val settingState = MutableLiveData<SettingsState>()


    init{
        updateThemeSwitcher()
    }

    fun getSettingsState(): LiveData<SettingsState> = settingState

    private fun updateThemeSwitcher() {
        settingState.postValue(SettingsState(settingsRepository.getThemeSettings().isDarkModeON))
    }

    fun saveThemePreference(checked: Boolean){
        settingsRepository.updateThemeSetting(checked)
      settingState.postValue(SettingsState(checked))
    }
    fun shareApp(){
        sharingInteractor.shareApp()
    }

    fun sendEmail(){
        sharingInteractor.sendEmail()
    }

    fun showAgreement(){
        sharingInteractor.showAgreement()
    }

}