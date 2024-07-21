package com.practicum.playlistmaker.player.data.impl

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.player.domain.api.TrackPlayerRepository
import com.practicum.playlistmaker.search.mapper.MillisConverter


class TrackPlayerRepositoryImpl(val mediaPlayer: MediaPlayer) : TrackPlayerRepository {
    companion object {
        const val STATE_DEFAULT = "0"
        const val STATE_PREPARED = "1"
        const val STATE_PLAYING = "2"
        const val STATE_PAUSED = "3"
        const val PLAYBACK_TIME_UPDATE_DELAY = 500L
    }

    //private val mediaPlayer = Creator.provideMediaPlayer()
    private val handler = Handler(Looper.getMainLooper())
    private var playerState = STATE_DEFAULT


    private lateinit var playbackTimer: Runnable
    override fun play(previewUrl: String, statusObserver: TrackPlayerRepository.StatusObserver) {
        initializePlayer(previewUrl)
        if (playerState == STATE_PREPARED || playerState == STATE_PAUSED) {
            playerState = STATE_PLAYING
            mediaPlayer.start()
        }

        statusObserver.onPlay()
        playbackTimer = object : Runnable {
            override fun run() {
                statusObserver.onProgress(updateCurrentPlaybackTime())
                handler.postDelayed(this, PLAYBACK_TIME_UPDATE_DELAY)

                if (playerState == STATE_PAUSED) {
                    statusObserver.onPause(updateCurrentPlaybackTime())
                    handler.removeCallbacks(playbackTimer)
                } else if (playerState == STATE_PREPARED) {
                    statusObserver.onStop()
                    handler.removeCallbacks(playbackTimer)
                }
            }
        }
        autoPlaybackTimer()
    }

    private fun autoPlaybackTimer() {
        handler.postDelayed(playbackTimer, PLAYBACK_TIME_UPDATE_DELAY)
    }

    override fun pause() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED

    }

    override fun seek(str: String) {
    }

    override fun release() {
        if (playerState != STATE_DEFAULT) {
            handler.removeCallbacks(playbackTimer)
            mediaPlayer.release()
        }
    }

    override fun initializePlayer(previewUrl: String) {

        if (playerState == STATE_DEFAULT) {
            mediaPlayer.setDataSource(previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = STATE_PLAYING
                mediaPlayer.start()
            }
            mediaPlayer.setOnCompletionListener {
                playerState = STATE_PREPARED
            }

            mediaPlayer.setOnErrorListener { mp, what, extra ->
                true
            }
        } else {
            mediaPlayer.start()
        }
    }

    override fun resume() {
        mediaPlayer.start()
    }

    private fun updateCurrentPlaybackTime(): String {
        return MillisConverter.millisToMinutesAndSeconds(mediaPlayer.currentPosition.toString())
    }
}