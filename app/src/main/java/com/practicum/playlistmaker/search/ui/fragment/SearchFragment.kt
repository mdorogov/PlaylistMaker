package com.practicum.playlistmaker.search.ui.fragment

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentLibraryBinding
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.main.ui.RootActivity
import com.practicum.playlistmaker.search.data.impl.SearchHistoryRepositoryImpl
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.search.ui.TrackAdapter
import com.practicum.playlistmaker.search.ui.state.SearchState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(){
    private val viewModel by viewModel<SearchViewModel>()
    var inputSearchText: String? = null
    private lateinit var inputEditText: EditText
    private lateinit var trackRecycler: RecyclerView
    private lateinit var deleteButton: ImageView
    private lateinit var statusView: LinearLayout
    private lateinit var statusImage: ImageView
    private lateinit var statusText: TextView
    private lateinit var updateButton: Button

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistoryView: LinearLayout
    private lateinit var historyRecycler: RecyclerView
    private lateinit var cleanHistoryButton: Button

    private var songs = ArrayList<Track>()
    private var historyTracks = ArrayList<Track>()

    private lateinit var searchHistoryAdapter: TrackAdapter

    private lateinit var progressBar: ProgressBar

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
        (activity as? RootActivity)?.setBottomNavigationView(true)
        initializeViews()
        initializeRecyclerViews()
        viewModel.loadTracksHistory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
          inputEditText.clearFocus()
    }


    override fun onPause() {
        super.onPause()
   // (activity as? RootActivity)?.setBottomNavigationView(true)
    }

    private fun showLoading() {
        searchHistoryView.visibility = View.GONE
        statusView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun render(state: SearchState?) {
        when (state) {
            is SearchState.Loading -> showLoading()
            is SearchState.Content -> showContent(state.foundTracks)
            is SearchState.Empty -> showStatusView(state.message, state.userRequest)
            is SearchState.Error -> showStatusView(state.errorMessage, state.userRequest)
            is SearchState.History -> updateHistoryTracks(state.historyTracks)
            else -> {}
        }
    }

    private fun updateHistoryTracks(updatedHistoryTracks: List<Track>) {
        historyTracks.clear()
        historyTracks.addAll(updatedHistoryTracks)
        searchHistoryAdapter.notifyDataSetChanged()
    }

    private fun showEmptyResults(message: String) {
        if (songs.isEmpty()) {
            progressBar.visibility = View.GONE
            showStatusView("Not Found", "userRequest")
        }
    }

    private fun showContent(foundTracks: List<Track>) {
        songs.clear()
        songs.addAll(foundTracks)
        progressBar.visibility = View.GONE
        trackRecycler.visibility = View.VISIBLE
        statusView.visibility = View.GONE
        trackRecycler.adapter = trackAdapter
        trackAdapter.notifyDataSetChanged()
    }

    private fun initializeRecyclerViews() {
        trackRecycler = binding.trackRecycler
        trackRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        trackAdapter = TrackAdapter(requireContext(), songs) {}
        searchHistoryView = binding.searchHistoryView
        cleanHistoryButton = binding.cleanHistoryButton

        historyRecycler = binding.searchHistoryRecycler
        historyRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        searchHistoryAdapter =
            TrackAdapter(requireContext(), historyTracks){}

        progressBar = binding.progressBar

        viewModel.loadTracksHistory()

        historyRecycler.adapter = searchHistoryAdapter

        setOnFocusUserInput()
        viewModel.setHistorySharedPrefListener()
        setOnClickCleanHistoryButton()
        setTextWatcher()
    }

    private fun setTextWatcher() {
        val inputMethodManager =
            requireContext().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    deleteButton.visibility = View.GONE
                    inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
                   // (activity as? RootActivity)?.setBottomNavigationView(false)
                    searchHistoryAdapter.notifyDataSetChanged()
                    inputEditText.clearFocus()
                } else {
                    searchHistoryView.visibility = View.GONE
                    deleteButton.visibility = View.VISIBLE
                    inputEditText.requestFocus()
                    inputSearchText = s.toString()
                    trackRecycler.visibility = View.GONE


                    viewModel.searchTracksDebounce(s.toString())
                    deleteButton.setOnClickListener {
                        statusView.visibility = View.GONE
                        inputEditText.setText("")
                        songs.clear()
                        trackAdapter.notifyDataSetChanged()
                        viewModel.loadTracksHistory()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun setOnClickCleanHistoryButton() {
        cleanHistoryButton.setOnClickListener {
            viewModel.cleanHistory()
            searchHistoryAdapter.notifyDataSetChanged()
            searchHistoryView.visibility = View.GONE
        }
    }

    private fun setOnFocusUserInput() {
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus){
                statusView.visibility = View.GONE
                showTracksHistory(hasFocus)
               // (activity as? RootActivity)?.setBottomNavigationView(false)

            } else {
                //(activity as? RootActivity)?.setBottomNavigationView(true)
            }
        }
    }

    private fun showTracksHistory(hasFocus: Boolean) {
        trackRecycler.visibility = View.GONE
        searchHistoryView.visibility = if (hasFocus && inputEditText.text.isEmpty()) {

            if (historyTracks.isNotEmpty()) {
                historyRecycler.adapter = searchHistoryAdapter
                searchHistoryAdapter.notifyDataSetChanged()
                View.VISIBLE
            } else View.GONE
        } else View.GONE
    }


    private fun initializeViews() {
        inputEditText = binding.searchEditText
        deleteButton = binding.deleteButton
        cleanHistoryButton = binding.cleanHistoryButton
        statusView = binding.searchStatusLayout
        inputEditText.doOnLayout { inputEditText.hint }
        deleteButton.setOnClickListener {
            inputEditText.setText("")
        }
        if (!inputSearchText.isNullOrEmpty()) {
            inputEditText.setText(inputSearchText)
        }

    }

    private fun showStatusView(typeOfMessage: String, inputUserText: String) {
        statusImage = binding.searchStatusImage
        statusText = binding.searchStatusText
        updateButton = binding.updateButton
        setProgressBarVisibilityOff()

        when (typeOfMessage) {
            "Not Found" -> {
                statusImage.setImageResource(R.drawable.not_found_error)
                statusText.setText(getString(R.string.not_found_text_ru))
                updateButton.visibility = View.GONE
            }

            "No Connection" -> {
                statusImage.setImageResource(R.drawable.connection_error)
                statusText.setText(getString(R.string.no_connection_text_ru))
                updateButton.setOnClickListener {
                    trackSearching()
                }
                updateButton.visibility = View.VISIBLE
            }
        }
        trackRecycler.visibility = View.GONE
        statusView.visibility = View.VISIBLE

    }

    private fun trackSearching() {


        var userRequest = inputEditText.text.toString()

        if (userRequest.isNotEmpty()) {
            setProgressBarVisibilityOn()
            viewModel.searchTracksDebounce(userRequest)
        }
    }

    private fun setProgressBarVisibilityOn() {
        progressBar.visibility = View.VISIBLE
    }

    private fun setProgressBarVisibilityOff() {
        progressBar.visibility = View.GONE
    }
}