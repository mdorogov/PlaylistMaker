package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener{
val backButtonIntent = Intent(this, MainActivity::class.java)
            startActivity(backButtonIntent)
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
            putExtra(Intent.EXTRA_EMAIL, getString(R.string.test_email))
            putExtra(Intent.EXTRA_SUBJECT, getString(R.string.subject_support_mail) +
                    " Playlist Maker")
            putExtra(Intent.EXTRA_TEXT, getString(R.string.text_support_mail))

        }
            startActivity(intent)
    }
}