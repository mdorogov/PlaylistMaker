package com.practicum.playlistmaker.player.ui

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.search.mapper.DimensConverter
import com.practicum.playlistmaker.search.mapper.MillisConverter
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.player.PlaylistAdapter
import com.practicum.playlistmaker.player.ui.state.PlayerState
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity() : AppCompatActivity() {
    private lateinit var viewModel: PlayerViewModel

    companion object {
        const val SHOWN_TRACK = "SHOWN_TRACK"
        const val SHOWN_DEF = "0"
    }

    private lateinit var json: String
    private lateinit var playButton: ImageView
    private lateinit var playtime: TextView
    private lateinit var favoriteTrackIcon: ImageView

    private var isTrackPlaying = false
    private var isActivityReady = false

    private lateinit var playlistRecycler: RecyclerView
    private lateinit var playlistAdapter: PlaylistAdapter
    val bottomSheetContainer = findViewById<LinearLayout>(R.id.add_to_playlist_bottom_sheet)
    val bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)





    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SHOWN_TRACK, json)
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        setPlayerViews()
        setBottomSheet()


        if (savedInstanceState != null) {
            json = savedInstanceState.getString(SHOWN_TRACK, SHOWN_DEF)
        } else {
            val intent = getIntent()
            intent.getStringExtra(Intent.EXTRA_SUBJECT)
            json = intent.getStringExtra(Intent.EXTRA_SUBJECT).toString()
        }
        viewModel = getViewModel<PlayerViewModel> {
            parametersOf(json)
        }



        viewModel.getScreenStateLiveData().observe(this) { screenState ->
            when (screenState) {
                is PlayerState.Loading -> changeContentVisibility(isVisible = true)
                is PlayerState.Content -> setPlayerContent(
                    screenState.trackModel,
                    true,
                    screenState.isTrackFavorite
                )

                is PlayerState.PlayTime -> setPlayStatus(screenState.progress, true)
                is PlayerState.PlayTimePaused -> setPlayStatus(screenState.progress, false)
                is PlayerState.PlayingStopped -> setPlayStatus(screenState.progress, false)
                is PlayerState.FavoriteTrackChanged -> setFavoriteTrackIcon(screenState.isTrackFavorite)
              is PlayerState.PlaylistChoosing -> openPlaylistsBottomSheet()
                is PlayerState.NewPlaylistCreated -> openPlaylistCreatingFragment()
            }
        }

    }

    private fun openPlaylistCreatingFragment() {
        playlistRecycler = findViewById<RecyclerView>(R.id.favoritePlaylistsSheetRecycler)
        playlistRecycler.layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        playlistAdapter = PlaylistAdapter(applicationContext, playlists)
    }

    private fun openPlaylistsBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setBottomSheet() {
       bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED



        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        loadPlaylists()
                    }
                    else -> {

                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        } )
    }

    private fun loadPlaylists() {
        TODO("Not yet implemented")

    }

    private fun changeContentVisibility(isVisible: Boolean) {
    }

    fun setPlayStatus(progress: String, isPlaying: Boolean) {
        changeButtonStyle(isPlaying)
        playtime.text = progress
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
        favoriteTrackIcon = findViewById<ImageView>(R.id.add_to_favorite_button)



        backButton.setOnClickListener {
            finish()
            viewModel.releasePlayer()
        }

        addToPlaylistButton.setOnClickListener {
            viewModel.addTrackToPlaylist()
        }
    }

    private fun setPlayerContent(
        trackModel: Track,
        isContentVisible: Boolean,
        isTrackFavorite: Boolean
    ) {
        if (isActivityReady) {
            return
        }
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
        setFavoriteTrackIcon(isTrackFavorite)
        setFavoriteTrackIconOnListener()
        isActivityReady = true

    }

    private fun setPlayButtonOnListener() {
        playButton.setOnClickListener {
            playerControl(isTrackPlaying)
        }
    }

    private fun setFavoriteTrackIconOnListener() {
        favoriteTrackIcon.setOnClickListener {
            viewModel.favoriteTrackIconIsPressed()
        }
    }

    private fun setFavoriteTrackIcon(isTrackFavorite: Boolean) {
        if (isTrackFavorite) {
            favoriteTrackIcon.setImageResource(R.drawable.favorite_true_icon)
        } else {
            favoriteTrackIcon.setImageResource(R.drawable.favorite_false_icon)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.releasePlayer()
    }

    private fun playerControl(isPlaying: Boolean) {
        when (isPlaying) {
            false -> {
                changeButtonStyle(true)
                isTrackPlaying = true
                viewModel.play()
            }

            true -> {
                changeButtonStyle(false)
                isTrackPlaying = false
                viewModel.pause()
            }
        }
    }

    private fun setReleaseYear(str: String): String {
        return str.substring(0, 4)
    }

    private fun setArtwork(imageUrl: String, artworkView: ImageView) {
        Glide.with(applicationContext)
            .load(imageUrl)
            .placeholder(R.drawable.artwork_placeholder)
            .transform(RoundedCorners(DimensConverter.dpToPx(8f, artworkView)))
            .into(artworkView)
    }

}
