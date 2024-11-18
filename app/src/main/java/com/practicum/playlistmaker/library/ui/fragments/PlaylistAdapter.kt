package com.practicum.playlistmaker.library.ui.fragments


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.PlaylistSheetViewBinding
import com.practicum.playlistmaker.player.ui.PlayerActivity
import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.library.ui.view_model.PlaylistViewHolder
import com.practicum.playlistmaker.player.domain.api.OnPlaylistClick
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlaylistAdapter(
    private val context: Context,
    private var playlists: ArrayList<Playlist>,
    private var onClick: OnPlaylistClick
) : RecyclerView.Adapter<PlaylistViewHolder>() {
    companion object {
        private const val SEARCHING_DELAY = 2000L
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {

        val layoutInspector = LayoutInflater.from(parent.context)
        return PlaylistViewHolder(PlaylistSheetViewBinding.inflate(layoutInspector, parent, false))
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            openPlaylistDebounce(position)
        }
    }

    private fun openPlaylistDebounce(position: Int) {
        if (isClickAllowed) {
            isClickAllowed = false


            onClick.onClick(position)
            CoroutineScope(Dispatchers.IO).launch {
                delay(SEARCHING_DELAY)
                isClickAllowed = true
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun update(playlists: ArrayList<Playlist>) {
        this.playlists.clear()
        this.playlists = playlists
        notifyDataSetChanged()
    }
}