package com.practicum.playlistmaker

import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Track

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



    private val trackName: TextView = itemView.findViewById(R.id.track_title)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackDuration: TextView = itemView.findViewById(R.id.track_duration)
    private val artwork: ImageView = itemView.findViewById(R.id.artwork)


    fun bind(model: Track) {
        trackName.text = model.trackName
        artistName.text = model.artistName
        trackDuration.text = model.trackDuration
        setArtwork(model.artwork)
    }

    private fun setArtwork(art: String) {
        Glide.with(artwork)
            .load(art)
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(DimensConverter.dpToPx(2f, artwork)))
            .into(artwork)
    }


}