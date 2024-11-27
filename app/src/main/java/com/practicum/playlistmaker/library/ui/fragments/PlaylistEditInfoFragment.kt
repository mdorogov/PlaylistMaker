package com.practicum.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.library.ui.fragments.PlaylistFragment.Companion.ARGS_PLAYLIST_ID
import com.practicum.playlistmaker.library.ui.state.PlaylistCreatingState
import com.practicum.playlistmaker.library.ui.state.PlaylistEditState
import com.practicum.playlistmaker.library.ui.view_model.PlaylistCreatingViewModel
import com.practicum.playlistmaker.library.ui.view_model.PlaylistEditInfoViewModel
import com.practicum.playlistmaker.main.ui.RootActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistEditInfoFragment : PlaylistCreatingFragment() {

    companion object {
        const val ARGS_PLAYLIST_EDIT = "ID"
        fun createArgs(playlistId: Long): Bundle = bundleOf(ARGS_PLAYLIST_EDIT to playlistId)
    }

    private var playlistId: Long = 0


    override val viewModel: PlaylistEditInfoViewModel by viewModel {
        parametersOf()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? RootActivity)?.setBottomNavigationView(false)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        playlistId = requireArguments().getLong(ARGS_PLAYLIST_EDIT)
        viewModel.loadPlaylistToEdit(playlistId)
    }

    private fun render(state: PlaylistCreatingState?) {
        when (state) {
            is PlaylistCreatingState.PlaylistEdit -> setEditViews(state.playlist)
            else -> {}
        }
    }

    private fun setEditViews(playlist: Playlist) {
        playlistArtwork.setImageURI(playlist.artwork.toUri())
        playlistNameEditText.editText?.setText(playlist.playlistName)
        playlistDescriptionEditText.editText?.setText(playlist.description)
        createPlaylistButton.setText("Сохранить")
    }

    override fun createPlaylist() {
        isArtworkChosen = true
        viewModel.updatePlaylist(
            playlistId, saveChosenArtwork(),
            playlistNameEditText.editText?.text.toString(),
            playlistDescriptionEditText.editText?.text.toString()
        )
        parentFragmentManager.popBackStack()
    }

    override fun openExitDialog() {
        parentFragmentManager.popBackStack()
    }


}