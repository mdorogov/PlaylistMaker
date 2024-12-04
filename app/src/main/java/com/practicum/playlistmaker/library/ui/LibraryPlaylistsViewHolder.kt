package com.practicum.playlistmaker.library.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistLibraryViewBinding
import com.practicum.playlistmaker.databinding.TrackViewBinding
import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.library.mapper.WordFormConverter
import com.practicum.playlistmaker.search.mapper.DimensConverter

class LibraryPlaylistsViewHolder(private val binding: PlaylistLibraryViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val artwork: ImageView = binding.libraryPlaylistArtwork

    fun bind(model: Playlist) {
        binding.libraryPlaylistName.text = model.playlistName
        binding.libraryPlaylistNumOfTracks.text = WordFormConverter.getTrackWordForm(model.numOfTracks)
        setArtwork(model.artwork)
    }

    private fun setArtwork(art: String) {
        Glide.with(artwork)
            .load(art)
            .placeholder(R.drawable.placeholder)
            .apply(
                RequestOptions().transform(
                    MultiTransformation(
                        CenterCrop(), RoundedCorners(DimensConverter.dpToPx(8f, artwork))
                    )
                )
            )
            .into(artwork)
    }
}