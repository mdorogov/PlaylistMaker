package com.practicum.playlistmaker.library.ui.view_model


import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.practicum.playlistmaker.search.mapper.DimensConverter
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.PlaylistSheetViewBinding
import com.practicum.playlistmaker.library.data.models.Playlist

class PlaylistViewHolder(private val binding: PlaylistSheetViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    private val artwork: ImageView = itemView.findViewById(R.id.artwork)


    fun bind(model: Playlist) {
        binding.playlistName.text = model.playlistName
        binding.numOfTracks.text = model.numOfTracks.toString()
        setArtwork(model.artwork)

    }

    private fun setArtwork(art: String) {
        Glide.with(artwork)
            .load(art)
            .placeholder(R.drawable.placeholder)
            .apply(
                RequestOptions()
                    .transform(
                        MultiTransformation(
                            CenterCrop(), RoundedCorners(DimensConverter.dpToPx(2f, artwork))
                        )
                    )
            )
            .into(artwork)
    }


}