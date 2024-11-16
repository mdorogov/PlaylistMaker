package com.practicum.playlistmaker.library.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import com.practicum.playlistmaker.databinding.PlaylistLibraryViewBinding
import com.practicum.playlistmaker.library.data.models.Playlist
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LibraryPlaylistAdapter(
    private val context: Context,
    private var playlists: List<Playlist>,
) : RecyclerView.Adapter<LibraryPlaylistsViewHolder>() {
    companion object {
        private const val SEARCHING_DELAY = 2000L
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibraryPlaylistsViewHolder {

        val layoutInspector = LayoutInflater.from(parent.context)
        return LibraryPlaylistsViewHolder(
            PlaylistLibraryViewBinding.inflate(
                layoutInspector,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: LibraryPlaylistsViewHolder, position: Int) {
        holder.bind(playlists[position])
        holder.itemView.setOnClickListener {
            openPlayerDebounce(playlists[position])
        }
    }

    private fun openPlayerDebounce(playlist: Playlist) {
        val current = isClickAllowed
    }
}