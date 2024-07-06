package com.practicum.playlistmaker.sharing.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class ExternalNavigator(var context: Context) :AppCompatActivity() {

    fun shareLink(shareAppLink: String) {
        val shareButtonIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareAppLink)
            type = "text/html"
        }
        val intent = Intent.createChooser(shareButtonIntent, null)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }


    fun openLink(termsLink: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(termsLink))
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)

    }

    fun openEmail(supportEmailData: EmailData) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            val sendToEmail = supportEmailData.sendToEmail
            putExtra(Intent.EXTRA_EMAIL, sendToEmail)
            putExtra(
                Intent.EXTRA_SUBJECT, supportEmailData.subject)
            putExtra(Intent.EXTRA_TEXT, supportEmailData.text)

        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}