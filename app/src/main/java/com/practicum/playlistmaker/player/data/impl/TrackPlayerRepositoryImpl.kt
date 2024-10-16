package com.practicum.playlistmaker.player.data.impl

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.player.domain.api.TrackPlayerRepository
import com.practicum.playlistmaker.search.mapper.MillisConverter


class TrackPlayerRepositoryImpl() : TrackPlayerRepository {
    companion object {
        const val STATE_DEFAULT = "0"
        const val STATE_PREPARED = "1"
        const val STATE_PLAYING = "2"
        const val STATE_PAUSED = "3"
        const val PLAYBACK_TIME_UPDATE_DELAY = 500L
    }

    private var mediaPlayer: MediaPlayer? = null

    private val handler = Handler(Looper.getMainLooper())
    private var playerState = STATE_DEFAULT

    override fun play(previewUrl: String) {
        initializePlayer(previewUrl)
        if (playerState == STATE_PREPARED || playerState == STATE_PAUSED) {
            playerState = STATE_PLAYING
            mediaPlayer?.start()
        }
    }

    override fun getPlayingStatus(): Boolean {
        return mediaPlayer!!.isPlaying
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


                setOnPreparedListener {
                    playerState = STATE_PLAYING
                    start()
                }
                setOnCompletionListener {
                    playerState = STATE_PREPARED
                    releasePlayer()
                }

                setOnErrorListener { mp, what, extra ->
                    true
                }
            }

        } else {
            mediaPlayer?.start()
        }
    }

    override fun resume() {
        mediaPlayer?.start()
    }

    override fun updateCurrentPlaybackTime(): String {
        return MillisConverter.millisToMinutesAndSeconds(mediaPlayer?.currentPosition.toString())
    }

    override fun getIsSongPlayed(): Boolean {
        if (playerState == STATE_PREPARED) {
            return true
        } else return false
    }

}