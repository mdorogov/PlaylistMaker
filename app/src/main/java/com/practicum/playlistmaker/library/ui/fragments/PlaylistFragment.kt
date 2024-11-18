package com.practicum.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.library.ui.state.PlaylistState
import com.practicum.playlistmaker.library.ui.view_model.PlaylistViewModel
import com.practicum.playlistmaker.library.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistFragment : Fragment() {

    companion object {
        const val ARGS_PLAYLIST_ID = "ID"

        fun createArgs(playlistId: Long): Bundle = bundleOf(ARGS_PLAYLIST_ID to playlistId)
    }

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val playlistViewModel: PlaylistViewModel by viewModel {
        parametersOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
         return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        playlistViewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }

        val playlistId = requireArguments().getLong(ARGS_PLAYLIST_ID)
        playlistViewModel.loadData(playlistId)
    }

    private fun render(state: PlaylistState?) {
when (state) {
    is PlaylistState.Content -> showContent(state.playlist)
    else -> {}
}
    }

    private fun showContent(playlist: Playlist) {
binding.playlistFragmentArtwork.setImageURI(playlist.artwork.toUri())
        binding.playlistFragmentName.setText(playlist.playlistName)
        binding.playlistFragmentDescription.setText(playlist.description) // проверить вылет, если будет пустое  описание
        binding.playlistFragmentDurationText.setText("playlist.")


    }
}