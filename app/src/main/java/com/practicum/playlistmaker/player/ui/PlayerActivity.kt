package com.practicum.playlistmaker.player.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.search.mapper.DimensConverter
import com.practicum.playlistmaker.search.mapper.MillisConverter
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.player.ui.state.PlayerState
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.data.models.Track

class PlayerActivity() : AppCompatActivity() {
    private lateinit var viewModel: PlayerViewModel

    companion object {
        const val SHOWN_TRACK = "SHOWN_TRACK"
        const val SHOWN_DEF = "0"
    }

    private lateinit var json: String
    private lateinit var playButton: ImageView
    private lateinit var playtime: TextView

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SHOWN_TRACK, json)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        setPlayerViews()

        if (savedInstanceState != null) {
            json = savedInstanceState.getString(SHOWN_TRACK, SHOWN_DEF)
        } else {
            val intent = getIntent()
            intent.getStringExtra(Intent.EXTRA_SUBJECT)
            json = intent.getStringExtra(Intent.EXTRA_SUBJECT).toString()
        }

        viewModel = ViewModelProvider(
            this,
            PlayerViewModel.getViewModelFactory(json)
        )[PlayerViewModel::class.java]

        viewModel.getScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is PlayerState.Loading -> changeContentVisibility(isVisible = true)
                is PlayerState.Content -> setPlayerContent(screenState.trackModel, true)
            }
        }

        viewModel.getPlayStatusLiveData().observe(this) { playStatus ->
            changeButtonStyle(playStatus.isPlaying)
            playtime.text = playStatus.progress
        }
    }

    private fun changeButtonStyle(isPlaying: Boolean) {
        if (isPlaying) {
            playButton.setImageResource(R.drawable.pause_button)
        } else {
            playButton.setImageResource(R.drawable.play_button)
        }
    }

    private fun setPlayerViews() {
        val backButton = findViewById<ImageView>(R.id.back_button)
        val addToPlaylistButton = findViewById<ImageView>(R.id.add_to_playlist_button)
        val addToFavoriteButton = findViewById<ImageView>(R.id.add_to_favorite_button)
        val durationView = findViewById<TextView>(R.id.duration_view)
        val albumTittleView = findViewById<TextView>(R.id.album_title_view)
        val releaseYearView = findViewById<TextView>(R.id.year_view)
        val genreView = findViewById<TextView>(R.id.genre_view)
        val countryView = findViewById<TextView>(R.id.country_view)
        playButton = findViewById(R.id.play_button)
        playtime = findViewById(R.id.current_playtime_view)

        backButton.setOnClickListener {
            finish()
            viewModel.releasePlayer()
        }
    }

    private fun setPlayerContent(trackModel: Track, isContentVisible: Boolean) {
        val artwork = findViewById<ImageView>(R.id.artwork_player)
        val artistName =
            findViewById<TextView>(R.id.artist_name_player).setText(trackModel.artistName)
        val albumTitleText =
            findViewById<TextView>(R.id.album_title_text).setText(trackModel.collectionName)
        val countryText = findViewById<TextView>(R.id.country_text).setText(trackModel.country)
        val genreText = findViewById<TextView>(R.id.genre_text).setText(trackModel.primaryGenreName)
        val durationText = findViewById<TextView>(R.id.track_duration_text)
            .setText(MillisConverter.millisToMinutesAndSeconds(trackModel.trackTimeMillis))
        val trackTitlePlayer =
            findViewById<TextView>(R.id.track_title_player).setText(trackModel.trackName)
        val releaseYearText =
            findViewById<TextView>(R.id.year_text).setText(setReleaseYear(trackModel.releaseDate))
        playtime.setText(trackModel.getPlayerTrackTime())
        setArtwork(trackModel.getPlayerArtwork(), artwork)
        setPlayButtonOnListener()

    }

    private fun setPlayButtonOnListener() {
        playButton.setOnClickListener {
            playerControl()
        }
    }

    private fun changeContentVisibility(isVisible: Boolean) {
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    private fun playerControl() {
        when (viewModel.getPlayStatusLiveData().value!!.isPlaying) {
            false -> {
                changeButtonStyle(true)
                viewModel.play()
            }

            true -> {
                changeButtonStyle(false)
                viewModel.pause()
            }
        }
    }

    fun setReleaseYear(str: String): String {
        return str.substring(0, 4)
    }

    fun setArtwork(imageUrl: String, artworkView: ImageView) {
        Glide.with(applicationContext)
            .load(imageUrl)
            .placeholder(R.drawable.artwork_placeholder)
            .transform(RoundedCorners(DimensConverter.dpToPx(8f, artworkView)))
            .into(artworkView)
    }

}
