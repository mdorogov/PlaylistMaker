package com.practicum.playlistmaker.player


import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.search.mapper.DimensConverter
import com.practicum.playlistmaker.search.mapper.MillisConverter
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistSheetViewBinding
import com.practicum.playlistmaker.databinding.TrackViewBinding
import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.search.data.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistViewHolder(private val binding: PlaylistSheetViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val artwork: ImageView = itemView.findViewById(R.id.artwork)


    fun bind(model: Playlist) {
        //ОБЛОЖКА ОБРАБОТКА СЮДА
        binding.playlistName.text = model.playlistName
        binding.numOfTracks.text = model.numOfTracks

    }

    private fun setArtwork(art: String) {
        Glide.with(artwork)
            .load(art)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(DimensConverter.dpToPx(2f, artwork)))
            .into(artwork)
    }



}