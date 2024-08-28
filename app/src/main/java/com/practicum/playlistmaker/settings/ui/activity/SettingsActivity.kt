package com.practicum.playlistmaker.settings.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val NIGHT_THEME_CHECKED = "key_for_theme_switcher"

class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var themeSwitcher: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_settings)
        themeSwitcher = findViewById(R.id.themeSwitcher)
        val shareButton = findViewById<FrameLayout>(R.id.shareButton)
        val supportButton = findViewById<FrameLayout>(R.id.supportButton)
        val agreementButton = findViewById<FrameLayout>(R.id.agreementButton)


viewModel.getSettingsState().observe(this){settingsState ->
    if (settingsState.isDarkModeOn){
        themeSwitcher.isChecked = true
    } else false

    changeThemeMode(settingsState.isDarkModeOn)
}




        themeSwitcher.setOnCheckedChangeListener{switcher, checked ->

            viewModel.saveThemePreference(checked)
        }


        shareButton.setOnClickListener{
            viewModel.shareApp()
        }


        supportButton.setOnClickListener{
            viewModel.sendEmail()
        }


        agreementButton.setOnClickListener{
            viewModel.showAgreement()
        }
    }

    private fun changeThemeMode(checked: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (checked){
                AppCompatDelegate.MODE_NIGHT_YES

            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}


