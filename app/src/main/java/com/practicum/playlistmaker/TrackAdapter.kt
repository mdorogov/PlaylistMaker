package com.practicum.playlistmaker

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.content.Intent
import com.google.gson.Gson

class TrackAdapter(
    private val context: Context,
    private var tracks: ArrayList<Track>,
    private val searchHistory: SearchHistory,
) :RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.track_view, parent,
            false
        )
        return TrackViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            searchHistory.addTrackToArray(tracks[position])
            notifyDataSetChanged()
            val playerIntent = Intent(context, PlayerActivity::class.java)
            playerIntent.putExtra(Intent.EXTRA_SUBJECT, Gson().toJson(tracks[position]))
            context.startActivity(playerIntent)

        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun update(tracks: ArrayList<Track>) {
        this.tracks.clear()
        this.tracks = tracks
        notifyDataSetChanged()
    }
}