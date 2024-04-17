package com.practicum.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlayerActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val backButton = findViewById<ImageView>(R.id.back_button)
        val artwork = findViewById<ImageView>(R.id.artwork_player)
        val trackTitlePlayer = findViewById<TextView>(R.id.track_title_player)
        val artistName = findViewById<TextView>(R.id.artist_name_player)

        val addToPlaylistButton = findViewById<ImageView>(R.id.add_to_playlist_button)
        val playButton = findViewById<ImageView>(R.id.play_button)
        val addToFavoriteButton = findViewById<ImageView>(R.id.add_to_favorite_button)

        val durationView= findViewById<TextView>(R.id.duration_view)
        val albumTittleView= findViewById<TextView>(R.id.album_title_view)
        val releaseYearView= findViewById<TextView>(R.id.year_view)
        val genreView= findViewById<TextView>(R.id.genre_view)
        val countryView= findViewById<TextView>(R.id.country_view)

        val durationText= findViewById<TextView>(R.id.track_duration_text)
        val albumTitleText= findViewById<TextView>(R.id.album_title_text)
        val releaseYearText= findViewById<TextView>(R.id.year_text)
        val genreText= findViewById<TextView>(R.id.genre_text)
        val countryText= findViewById<TextView>(R.id.country_text)
    }
}