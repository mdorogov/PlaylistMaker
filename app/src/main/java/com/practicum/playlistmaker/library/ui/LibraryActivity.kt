package com.practicum.playlistmaker.library.ui

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.data.impl.TrackPlayerImpl
import com.practicum.playlistmaker.player.domain.api.TrackPlayer

class LibraryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)




      /*  mediaPlayer.play(
            "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview211/v4/b6/9d/62/b69d6256-479b-f1e7-4a57-067380a0ffc5/mzaf_17640929282047226595.plus.aac.p.m4a",
            statusObserver = object : TrackPlayer.StatusObserver {
                override fun onProgress(progress: String) {
                   // playStatusLiveData.value = getCurrentPlayStatus().copy(progress = progress)
                }

                override fun onStop() {
                   // playStatusLiveData.value = getCurrentPlayStatus().copy(isPlaying = false)
                }

                override fun onPlay() {
                }
            })*/
    }
}