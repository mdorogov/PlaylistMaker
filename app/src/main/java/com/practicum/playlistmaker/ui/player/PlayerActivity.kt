package com.practicum.playlistmaker.ui.player

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.gson.Gson
import com.practicum.playlistmaker.data.mapper.DimensConverter
import com.practicum.playlistmaker.data.mapper.MillisConverter
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.data.models.Track
import com.practicum.playlistmaker.ui.search.SearchActivity

const val STATE_DEFAULT = "0"
const val STATE_PREPARED = "1"
const val STATE_PLAYING = "2"
const val STATE_PAUSED = "3"
const val PLAYBACK_TIME_UPDATE_DELAY = 500L

class PlayerActivity() : AppCompatActivity() {
    companion object {
        const val SHOWN_TRACK = "SHOWN_TRACK"
        const val SHOWN_DEF = "0"

    }

    private lateinit var track: Track
    private lateinit var artwork: ImageView
    private lateinit var json: String

    private var playerRepository = Creator.providePlayer()
    private var mediaPlayer = playerRepository.getPlayer()
    private var playerState = STATE_DEFAULT

    private lateinit var playButton: ImageView
    private lateinit var playbackTimer: Runnable
    private lateinit var playtime: TextView

    private val handler = Handler(Looper.getMainLooper())

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SHOWN_TRACK, json)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        if (savedInstanceState != null) {
            json = savedInstanceState.getString(SHOWN_TRACK, SHOWN_DEF)
        } else {
            val intent = getIntent()
            json = intent.getStringExtra(Intent.EXTRA_SUBJECT).toString()
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        track = Gson().fromJson(json, Track::class.java)

        val backButton = findViewById<ImageView>(R.id.back_button)
        artwork = findViewById(R.id.artwork_player)
        val trackTitlePlayer =
            findViewById<TextView>(R.id.track_title_player).setText(track.trackName)
        val artistName = findViewById<TextView>(R.id.artist_name_player).setText(track.artistName)

        setArtwork()

        val addToPlaylistButton = findViewById<ImageView>(R.id.add_to_playlist_button)
        playButton = findViewById(R.id.play_button)
        val addToFavoriteButton = findViewById<ImageView>(R.id.add_to_favorite_button)
        playtime = findViewById(R.id.current_playtime_view)
        playtime.setText(MillisConverter.millisToMinutesAndSeconds(track.trackTimeMillis))

        val durationView = findViewById<TextView>(R.id.duration_view)
        val albumTittleView = findViewById<TextView>(R.id.album_title_view)
        val releaseYearView = findViewById<TextView>(R.id.year_view)
        val genreView = findViewById<TextView>(R.id.genre_view)
        val countryView = findViewById<TextView>(R.id.country_view)

        val durationText = findViewById<TextView>(R.id.track_duration_text)
            .setText(MillisConverter.millisToMinutesAndSeconds(track.trackTimeMillis))
        val albumTitleText =
            findViewById<TextView>(R.id.album_title_text).setText(track.collectionName)
        val releaseYearText =
            findViewById<TextView>(R.id.year_text).setText(setReleaseYear(track.releaseDate))
        val genreText = findViewById<TextView>(R.id.genre_text).setText(track.primaryGenreName)
        val countryText = findViewById<TextView>(R.id.country_text).setText(track.country)

        playbackTimer = object : Runnable {
            override fun run() {
                setCurrentPlaybackTime()
                handler.postDelayed(this, PLAYBACK_TIME_UPDATE_DELAY)
            }
        }

        playerPreparing()

        backButton.setOnClickListener {
            val backButtonIntent = Intent(this, SearchActivity::class.java)
            startActivity(backButtonIntent)
        }

        playButton.setOnClickListener {
            playerControl()
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    private fun playerPreparing() {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playButton.setImageResource(R.drawable.play_button)
            playerState = STATE_PREPARED
        }

        mediaPlayer.setOnCompletionListener {
            playButton.setImageResource(R.drawable.play_button)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        autoPlaybackTimer()
        playButton.setImageResource(R.drawable.pause_button)
        playerState = STATE_PLAYING
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        playButton.setImageResource(R.drawable.play_button)
        playerState = STATE_PAUSED
        handler.removeCallbacks(playbackTimer)
    }

    private fun playerControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }

        }
    }

    private fun autoPlaybackTimer() {
        handler.postDelayed(playbackTimer, PLAYBACK_TIME_UPDATE_DELAY)
    }

    private fun setCurrentPlaybackTime() {
        when (playerState) {
            STATE_PLAYING, STATE_PAUSED -> {
                playtime.setText(MillisConverter.millisToMinutesAndSeconds(mediaPlayer.currentPosition.toString()))
            }

            STATE_PREPARED -> {
                playtime.setText(getString(R.string.prepared_playback_time_text))
                handler.removeCallbacks(playbackTimer)
            }
        }
    }

    private fun setReleaseYear(str: String): String {
        return str.substring(0, 4)
    }

    fun setArtwork() {
        Glide.with(applicationContext)
            .load(track.getPlayerArtwork())
            .placeholder(R.drawable.artwork_placeholder)
            .transform(RoundedCorners(DimensConverter.dpToPx(8f, artwork)))
            .into(artwork)
    }
}