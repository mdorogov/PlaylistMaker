package com.practicum.playlistmaker.search.ui

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.search.mapper.DimensConverter
import com.practicum.playlistmaker.search.mapper.MillisConverter
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.TrackViewBinding
import com.practicum.playlistmaker.search.data.models.Track
import java.text.SimpleDateFormat
import java.util.Locale

class TrackViewHolder(private val binding: TrackViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val artwork: ImageView = itemView.findViewById(R.id.artwork)


    fun bind(model: Track) {
        binding.trackTitle.text = model.trackName
        binding.artistName.text = model.artistName
        binding.trackDuration.text =
            MillisConverter.millisToMinutesAndSeconds(model.trackTimeMillis)
        setArtwork(model.artworkUrl100)

    }

    private fun setArtwork(art: String) {
        Glide.with(artwork)
            .load(art)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(DimensConverter.dpToPx(2f, artwork)))
            .into(artwork)
    }

    private fun setDuration(millis: String): String {
        val num = millis.toLong()
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(num)

    }


}