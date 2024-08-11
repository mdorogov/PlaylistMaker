package com.practicum.playlistmaker.settings.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val NIGHT_THEME_CHECKED = "key_for_theme_switcher"

class SettingsActivity : AppCompatActivity() {

    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var themeSwitcher: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        themeSwitcher = findViewById(R.id.themeSwitcher)
        val shareButton = findViewById<FrameLayout>(R.id.share_button)
        val supportButton = findViewById<FrameLayout>(R.id.support_button)
        val agreementButton = findViewById<FrameLayout>(R.id.agreement_button)


viewModel.getSettingsState().observe(this){settingsState ->
    if (settingsState.isDarkModeOn){
        themeSwitcher.isChecked = true
    } else false

    changeThemeMode(settingsState.isDarkModeOn)
}

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener{
           finish()
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


