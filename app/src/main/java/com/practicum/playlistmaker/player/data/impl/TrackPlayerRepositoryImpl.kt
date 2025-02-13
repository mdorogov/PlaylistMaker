package com.practicum.playlistmaker.player.data.impl

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.player.domain.api.TrackPlayerRepository
import com.practicum.playlistmaker.search.mapper.MillisConverter
import org.koin.core.instance.InstanceFactory


class TrackPlayerRepositoryImpl() : TrackPlayerRepository {
    companion object {
        const val STATE_DEFAULT = "0"
        const val STATE_PREPARED = "1"
        const val STATE_PLAYING = "2"
        const val STATE_PAUSED = "3"
        const val STATE_PLAYED = "4"
        const val PLAYBACK_TIME_UPDATE_DELAY = 500L
    }

    private var mediaPlayer: MediaPlayer? = null

    private val handler = Handler(Looper.getMainLooper())
    private var playerState = STATE_DEFAULT
    private var initializedUrl = "null"

    override fun play() {
        if (playerState != STATE_DEFAULT) {
            playerState = STATE_PLAYING
            mediaPlayer?.start()
        } else initializePlayer(initializedUrl)
    }

    override fun getPlayingStatus(): Boolean {
        if (playerState == STATE_PLAYING) return true
        else return false
    }

    override fun pause() {
        mediaPlayer?.pause()
        playerState = STATE_PAUSED
    }

    override fun seek(str: String) {
    }

    override fun releasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        playerState = STATE_DEFAULT
    }

    override fun initializePlayer(previewUrl: String) {
        if (playerState == STATE_DEFAULT) {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(previewUrl)
                prepareAsync()
                initializedUrl = previewUrl

               setOnPreparedListener {
                    playerState = STATE_PREPARED
                  // start()
                }
                setOnCompletionListener {
                    playerState = STATE_PLAYED
                  //  releasePlayer()
                }

                setOnErrorListener { mp, what, extra ->
                    true
                }
            }

        }
    }

    override fun resume() {
        mediaPlayer?.start()
    }

    override fun updateCurrentPlaybackTime(): String {
       var current: Int? = mediaPlayer?.currentPosition
        if (current != null){
            return MillisConverter.millisToMinutesAndSeconds(current.toString())
        } else return "00:00"

    }

    override fun getIsSongPlayed(): Boolean {
        if (playerState == STATE_PLAYED) {
            return true
        } else return false
    }

}