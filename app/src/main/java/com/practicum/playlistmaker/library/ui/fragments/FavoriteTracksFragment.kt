package com.practicum.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavoriteTracksBinding
import com.practicum.playlistmaker.library.ui.state.FavoriteTracksState
import com.practicum.playlistmaker.library.ui.state.PlaylistsState
import com.practicum.playlistmaker.library.ui.view_model.FavoriteTracksViewModel
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class FavoriteTracksFragment : Fragment() {

    private lateinit var trackRecycler: RecyclerView
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var statusView: LinearLayout

    private var tracks = ArrayList<Track>()

    companion object {
        fun newInstance() = FavoriteTracksFragment().apply {
            arguments = Bundle().apply {  }
        }
    }

    private var _binding: FragmentFavoriteTracksBinding? = null
    private val binding get() = _binding!!

    private val favoriteTracksViewModel: FavoriteTracksViewModel by viewModel{
        parametersOf()
    }




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteTracksBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteTracksViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        initializeRecyclerViews()
        favoriteTracksViewModel.loadData()

    }

    private fun initializeRecyclerViews() {
        trackRecycler = binding.favoriteTrackRecycler
        statusView = binding.favTracksStatusLayout
        trackRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        trackAdapter = TrackAdapter(requireContext(), tracks)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: FavoriteTracksState?) {
        when (state) {
            is FavoriteTracksState.Content -> showContent(state.favoriteTracks)
            is FavoriteTracksState.ContentNotFound -> showError(state.stringRes)
            else -> {}
        }
    }

    private fun showError(stringRes: Int) {
        when(stringRes) {
            1 -> {
                statusView.visibility = View.VISIBLE
                _binding!!.favTracksErrorTxt.setText(R.string.fav_tracks_not_found_txt)
            }
            else -> {}
        }
    }

    private fun showContent(favTracks: List<Track>) {
        tracks.clear()
        tracks.addAll(favTracks)
        trackRecycler.visibility = View.VISIBLE
        statusView.visibility = View.GONE

        trackRecycler.adapter = trackAdapter
        trackAdapter.notifyDataSetChanged()
    }



}