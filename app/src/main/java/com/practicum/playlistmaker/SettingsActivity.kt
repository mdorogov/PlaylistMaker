package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.switchmaterial.SwitchMaterial

const val PLAYLIST_MAKER_PREFERENCES = "playlist_maker_preferences"
const val NIGHT_THEME_CHECKED = "key_for_theme_switcher"

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)



        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener{
           finish()
        }

        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)
        themeSwitcher.setOnCheckedChangeListener{switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            saveThemePreference(checked)
        }

        val shareButton = findViewById<FrameLayout>(R.id.share_button)
        shareButton.setOnClickListener{
            shareApp()
        }

        val supportButton = findViewById<FrameLayout>(R.id.support_button)
        supportButton.setOnClickListener{
            sendEmail()
        }

        val agreementButton = findViewById<FrameLayout>(R.id.agreement_button)
        agreementButton.setOnClickListener{
            showAgreement()
        }
    }

    private fun saveThemePreference(checked : Boolean) {
        val sharedPrefs = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        sharedPrefs.edit().putString(NIGHT_THEME_CHECKED, checked.toString()).apply()
    }

    private fun showAgreement() {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.agreement_link)))
        startActivity(intent)
    }

    private fun shareApp() {
        val shareButtonIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, getString(R.string.share_app_link))
            type = "text/html"
        }
        val intent = Intent.createChooser(shareButtonIntent, null)
        startActivity(intent)
    }

    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            val sendToEmail : Array<String> = arrayOf("dorogovmax@yandex.com")
            putExtra(Intent.EXTRA_EMAIL, sendToEmail)
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_support_mail) +
                    " Playlist Maker")
            putExtra(Intent.EXTRA_TEXT, getString(R.string.text_support_mail))

        }
            startActivity(intent)
    }
}


