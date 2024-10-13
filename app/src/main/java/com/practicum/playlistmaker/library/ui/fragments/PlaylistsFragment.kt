package com.practicum.playlistmaker.library.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.transition.Visibility
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.library.ui.state.PlaylistsState
import com.practicum.playlistmaker.library.ui.view_model.PlaylistsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlaylistsFragment : Fragment() {
    companion object {
        fun newInstance() = PlaylistsFragment().apply {
            arguments = Bundle().apply { }
        }
    }

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    private val playlistViewModel: PlaylistsViewModel by viewModel {
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
        playlistViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        playlistViewModel.loadData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: PlaylistsState?) {
        when (state) {
            is PlaylistsState.Content -> showContent()
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

    private fun showContent() {

    }


}