package com.practicum.playlistmaker.sharing.domain.impl

import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.domain.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
) : SharingInteractor {
    override fun shareApp() {
        externalNavigator.shareLink(getShareAppLink())
    }



    override fun showAgreement() {
        externalNavigator.openLink(getTermsLink())
    }



    override fun sendEmail() {
       externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(arrayOf("dorogovmax@yandex.com"), "Сообщение разработчикам и разработчицам приложения Playlist Maker",
            "Спасибо разработчикам и разработчицам за крутое приложение!")

//        return EmailData(arrayOf("dorogovmax@yandex.com"), getString(R.string.subject_support_mail),
//            getString(R.string.text_support_mail))
    }
    private fun getShareAppLink(): String {
        return "https://practicum.yandex.ru/learn/android-developer/"
            //return getString(R.string.share_app_link)
    }
    private fun getTermsLink(): String {
        return "https://yandex.ru/legal/practicum_offer/"
        //return getString(R.string.agreement_link)
    }
}