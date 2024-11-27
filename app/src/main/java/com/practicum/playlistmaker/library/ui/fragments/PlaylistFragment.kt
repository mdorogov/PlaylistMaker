package com.practicum.playlistmaker.library.ui.fragments

import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.library.domain.api.OnLongTrackClick
import com.practicum.playlistmaker.library.mapper.WordFormConverter
import com.practicum.playlistmaker.library.ui.state.PlaylistState
import com.practicum.playlistmaker.library.ui.view_model.PlaylistEditInfoViewModel
import com.practicum.playlistmaker.library.ui.view_model.PlaylistViewModel
import com.practicum.playlistmaker.main.ui.RootActivity
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.mapper.MillisConverter
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.w3c.dom.Text

class PlaylistFragment : Fragment(), OnLongTrackClick {

    companion object {
        const val ARGS_PLAYLIST_ID = "ID"

        fun createArgs(playlistId: Long): Bundle = bundleOf(ARGS_PLAYLIST_ID to playlistId)
    }

    private var savedId: Long? = null

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!


    private lateinit var playlist: Playlist
    private var tracks = ArrayList<Track>()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var bottomSettingsSheetBehavior: BottomSheetBehavior<View>
    private lateinit var trackAdapter: TrackAdapter

    private val playlistViewModel: PlaylistViewModel by viewModel {
        parametersOf()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)

        savedInstanceState?.let {
            var savedId = it.getString("SAVED_ID")
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as? RootActivity)?.setBottomNavigationView(false)

        playlistViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        val playlistId = requireArguments().getLong(ARGS_PLAYLIST_ID)
        playlistViewModel.loadData(playlistId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("SAVED_ID", playlist.id)
    }

    override fun onResume() {
        super.onResume()

        if (savedId?.equals(null) == false) {
            playlistViewModel.loadData(savedId!!)
        }

    }


    private fun render(state: PlaylistState?) {
        when (state) {
            is PlaylistState.Content -> showContent(
                state.playlist,
                state.tracks,
                state.tracksDuration
            )

            is PlaylistState.TrackIsDeleted -> showTrackIsDeletedToast()
            is PlaylistState.PlaylistIsDeleted -> moveToLibraryFragment()
            else -> {}
        }
    }

    private fun moveToLibraryFragment() {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(R.id.rootFragmentContainerView, LibraryFragment())
            ?.addToBackStack(null)
            ?.commit()
    }

    private fun showTrackIsDeletedToast() {
        Toast.makeText(requireContext(), "Трек удалён", Toast.LENGTH_SHORT)
    }

    private fun showContent(playlist: Playlist, tracks: List<Track>?, tracksDuration: Long) {
        this.playlist = playlist
        if (tracks != null) {
            this.tracks.clear()
            this.tracks.addAll(tracks)
        }


        binding.playlistFragmentArtwork.setImageURI(playlist.artwork.toUri())
        binding.playlistFragmentName.setText(playlist.playlistName)
        binding.playlistFragmentDescription.setText(playlist.description)
        binding.playlistFragmentDurationText.setText(
            WordFormConverter.getDurationWordForm(
                MillisConverter.millisToMinutes(tracksDuration)
            )
        )
        binding.playlistFragmentTracksQuantityText.setText(
            WordFormConverter.getTrackWordForm(
                playlist.numOfTracks
            )
        )

        binding.playlistFragmentShareButton.setOnClickListener {
            sharePlaylist(tracks.isNullOrEmpty())
        }

        binding.playlistFragmentMenu.setOnClickListener {
            openBottomSheetMenu()
        }

        setSettingsBottomSheetView()
        setBottomSheetView()

        showPlaylistTracks()
    }

    private fun openBottomSheetMenu() {
        val overlay = binding.overlay
        overlay.visibility = View.VISIBLE
        bottomSettingsSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        bottomSettingsSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    overlay.visibility = View.GONE
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }

        })
    }

    private fun sharePlaylist(isPlaylistEmpty: Boolean) {
        if (isPlaylistEmpty) {
            Toast.makeText(
                requireContext(),
                "В этом плейлисте нет списка треков, которым можно поделиться",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            playlistViewModel.sharePlaylist(playlist.id)
        }
    }

    private fun showPlaylistTracks() {
        val tracksRecycler = binding.playlistFragmentTracksRecycler
        tracksRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        trackAdapter = TrackAdapter(requireContext(), this.tracks, this)
        tracksRecycler.adapter = trackAdapter
        trackAdapter.notifyDataSetChanged()
    }

    private fun setBottomSheetView() {
        var bottomSheetContainer = binding.playlistFragmentTracksBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED


        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })


    }

    private fun setSettingsBottomSheetView() {
        var bottomSheetContainer = binding.playlistFragmentSettingsBottomSheet
        bottomSettingsSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)

        bottomSettingsSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.playlistFragmentSettingsSheet.playlistCardView.cardElevation = 0f
        binding.playlistFragmentSettingsSheet.playlistName.setText(playlist.playlistName)
        binding.playlistFragmentSettingsSheet.numOfTracks.text =
            WordFormConverter.getTrackWordForm(playlist.numOfTracks)
        binding.playlistFragmentSettingsSheet.artwork.setImageURI(playlist.artwork.toUri())

        binding.playlistFragmentShareSheet.setOnClickListener {
            sharePlaylist(tracks.isNullOrEmpty())
        }

        binding.playlistFragmentEditSheet.setOnClickListener {
            findNavController().navigate(
                R.id.action_playlistFragment_to_playlistEditInfoFragment,
                PlaylistEditInfoFragment.createArgs(playlist.id)
            )
        }

        binding.playlistFragmentDeleteSheet.setOnClickListener {
            showDeletePlaylistDialog()
        }
    }

    private fun showDeletePlaylistDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Хотите удалить плейлист ${playlist.playlistName}?")
            .setNegativeButton("Нет") { dialog, which ->
            }
            .setPositiveButton("Да") { dialog, which ->
                playlistViewModel.deletePlaylist(playlist.id)
                moveToLibraryFragment()
            }
            .show()
    }

    override fun onLongClicker(trackId: Int) {
        showDeletedTrackDialog(trackId)
    }

    private fun showDeletedTrackDialog(trackId: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Хотите удалить трек?")
            .setNegativeButton("Нет") { dialog, which ->
            }
            .setPositiveButton("Да") { dialog, which ->
                playlistViewModel.deleteTrackFromPlaylist(trackId, playlist.id)
            }
            .show()
    }
}
