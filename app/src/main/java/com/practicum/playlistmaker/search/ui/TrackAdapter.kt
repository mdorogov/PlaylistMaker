package com.practicum.playlistmaker.search.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.databinding.TrackViewBinding

class TrackAdapter(
    private val context: Context,
    private var tracks: ArrayList<Track>,
) : RecyclerView.Adapter<TrackViewHolder>() {
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {

        val layoutInspector = LayoutInflater.from(parent.context)
        return TrackViewHolder(TrackViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            openPlayerDebounce(tracks[position])
        }
    }

    private fun openPlayerDebounce(track: Track) {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            val playerIntent = Intent(context, PlayerActivity::class.java)
            playerIntent.putExtra(Intent.EXTRA_SUBJECT, Gson().toJson(track))
            context.startActivity(playerIntent)
            handler.postDelayed({ isClickAllowed = true }, 2000L)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun update(tracks: ArrayList<Track>) {
        this.tracks.clear()
        this.tracks = tracks
        notifyDataSetChanged()
    }
}