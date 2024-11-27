package com.practicum.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.library.data.models.Playlist
import com.practicum.playlistmaker.library.ui.LibraryPlaylistAdapter
import com.practicum.playlistmaker.library.ui.state.PlaylistsState
import com.practicum.playlistmaker.library.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.main.ui.RootActivity
import com.practicum.playlistmaker.player.domain.api.OnPlaylistClick
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistsFragment : Fragment(), OnPlaylistClick {
    companion object {
        fun newInstance() = PlaylistsFragment().apply {
            arguments = Bundle().apply { }
        }
    }

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlists: List<Playlist>


    private val playlistsViewModel: PlaylistsViewModel by viewModel {
        parametersOf()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playlistsViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        playlistsViewModel.loadData()
        binding.createPlaylistButtonFragment.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.rootFragmentContainerView, PlaylistCreatingFragment())
                ?.addToBackStack(null)
                ?.commit()

            (activity as? RootActivity)?.setBottomNavigationView(false)
        }


    }

    override fun onResume() {
        super.onResume()
        (activity as? RootActivity)?.setBottomNavigationView(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: PlaylistsState?) {
        when (state) {
            is PlaylistsState.Content -> showContent(state.playlists)
            is PlaylistsState.ContentNotFound -> showError(state.stringRes)
            else -> {}
        }
    }

    private fun showError(stringRes: Int) {
        when (stringRes) {
            1 -> {
                _binding!!.playlistsStatusLayout.visibility = View.VISIBLE
                _binding!!.playlistsErrorTxt.setText(R.string.fragment_list_not_found_text)
            }

            else -> {}
        }
    }

    private fun showContent(playlists: List<Playlist>) {
        binding.playlistsStatusLayout.visibility = View.GONE

        this.playlists = playlists

        val recyclerView = binding.allPlaylistsRecycler
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        var playlistAdapter = LibraryPlaylistAdapter(requireContext(), playlists, this)
        recyclerView.adapter = playlistAdapter
        playlistAdapter.notifyDataSetChanged()
        recyclerView.visibility = View.VISIBLE

    }

    override fun onClick(int: Int) {
        val playlistId = playlists[int].id
        findNavController().navigate(
            R.id.action_libraryFragment_to_playlistFragment,
            PlaylistFragment.createArgs(playlistId)
        )
    }


}