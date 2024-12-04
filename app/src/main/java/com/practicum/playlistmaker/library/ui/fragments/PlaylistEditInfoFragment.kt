package com.practicum.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.library.ui.state.PlaylistCreatingState
import com.practicum.playlistmaker.library.ui.view_model.PlaylistEditInfoViewModel
import com.practicum.playlistmaker.main.ui.RootActivity
import com.practicum.playlistmaker.search.mapper.DimensConverter
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
            is PlaylistCreatingState.PlaylistIsUpdated -> showPlaylistFragment(state.isUpdated)
            else -> {}
        }
    }

    private fun showPlaylistFragment(isUpdated: Boolean) {
       if (isUpdated){
           findNavController().popBackStack()
       }

    }

    private fun setEditViews(playlist: Playlist) {
        artworkUri = playlist.artwork.toUri()
        setPlaylistArtwork()
        playlistNameEditText.editText?.setText(playlist.playlistName)
        playlistDescriptionEditText.editText?.setText(playlist.description)
        createPlaylistButton.setText("Сохранить")
    }

    override fun createPlaylist() {
        viewModel.updatePlaylist(
            playlistId, artworkUri,
            playlistNameEditText.editText?.text.toString(),
            playlistDescriptionEditText.editText?.text.toString()
        )

    }

    private fun setPlaylistArtwork() {
        Glide.with(playlistArtwork)
            .load(artworkUri.toString())
            .placeholder(R.drawable.placeholder)
            .apply(
                RequestOptions().transform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(DimensConverter.dpToPx(8f, playlistArtwork))
                    )
                )
            )
            .into(playlistArtwork)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun openExitDialog() {
        parentFragmentManager.popBackStack()
    }


}