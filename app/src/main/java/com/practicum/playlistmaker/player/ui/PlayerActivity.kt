package com.practicum.playlistmaker.player.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.search.mapper.DimensConverter
import com.practicum.playlistmaker.search.mapper.MillisConverter
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.library.ui.fragments.PlaylistAdapter
import com.practicum.playlistmaker.library.ui.fragments.PlaylistCreatingFragment
import com.practicum.playlistmaker.player.ui.state.PlayerState
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.data.models.Track
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


    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var playlists = arrayListOf<Playlist>()


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

        val navHostPlayerFragment =
            supportFragmentManager.findFragmentById(R.id.playlist_creating_container_view) as NavHostFragment
        val navPlayerController = navHostPlayerFragment.navController



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
                is PlayerState.PlaylistChoosing -> openPlaylistsBottomSheet(screenState.playlists)
                is PlayerState.TrackIsAdded -> showTrackAddedToast(
                    screenState.isTrackAdded,
                    screenState.nameOfTrack,
                    screenState.nameOfPlaylist
                )

                is PlayerState.NewPlaylistCreated -> openPlaylistCreatingFragment()
                is PlayerState.PlaylistUpdate -> notifyAdapterOnUpdates(playlists)
            }
        }

    }

    private fun notifyAdapterOnUpdates(playlists: ArrayList<Playlist>) {
        this.playlists = playlists
        if (this::playlistAdapter.isInitialized) {
            playlistAdapter.notifyDataSetChanged()
        }
    }

    private fun showTrackAddedToast(
        isTrackAdded: Boolean,
        trackName: String,
        playlistName: String
    ) {
        if (isTrackAdded) {
            Toast.makeText(this, trackName + " добавлен в " + playlistName, Toast.LENGTH_SHORT)
                .show()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        } else {
            Toast.makeText(
                this,
                trackName + " уже существует в подборке " + playlistName,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openPlaylistCreatingFragment() {
    }

    private fun openPlaylistsBottomSheet(playlists: ArrayList<Playlist>?) {

        if (!playlists.isNullOrEmpty()) {
            playlistRecycler = findViewById<RecyclerView>(R.id.favoritePlaylistsSheetRecycler)
            playlistRecycler.layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            this.playlists = playlists

            playlistAdapter = PlaylistAdapter(applicationContext, playlists) { itemId ->
                onClick(itemId)

            }
            playlistRecycler.adapter = playlistAdapter
            playlistAdapter.notifyDataSetChanged()
        }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        var createButton = findViewById<Button>(R.id.createPlaylistSheetButton).setOnClickListener {

            val fragment = PlaylistCreatingFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.playlist_creating_container_view, fragment)
                .addToBackStack(null)
                .commit()
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

    }

    private fun setBottomSheet() {
        val bottomSheetContainer = findViewById<LinearLayout>(R.id.add_to_playlist_bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        val overlay = findViewById<View>(R.id.overlay)

        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        overlay.visibility = View.VISIBLE
                    }

                    else -> {
                        overlay.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
        })
    }

    private fun changeContentVisibility(isVisible: Boolean) {
    }

    fun setPlayStatus(progress: String, isPlaying: Boolean) {
        isTrackPlaying = isPlaying
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
            viewModel.showBottomSheet()
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

    fun onClick(int: Int) {
        val playlistId = playlists[int].id
        viewModel.addTrackToPlaylist(playlistId)
    }

}
