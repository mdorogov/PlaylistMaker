package com.practicum.playlistmaker.search.ui

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.data.models.Track
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.search.domain.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.TracksInteractor
import com.practicum.playlistmaker.search.ui.state.SearchState
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel


const val SEARCH_TRACK_HISTORY = "search_track_history"
const val JSON_HISTORY_KEY = "key_for_json_history"


class SearchActivity : AppCompatActivity() {

    private lateinit var viewModel: SearchViewModel
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
    private lateinit var tracksInteractor: TracksInteractor

    private lateinit var binding: ActivitySearchBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tracksInteractor = Creator.provideTracksInteractor(this)
        viewModel = ViewModelProvider(
            this,
            SearchViewModel.getViewModelFactory()
        )[SearchViewModel::class.java]

        viewModel.observeState().observe(this) {
            render(it)
        }

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        initializeViews()
        initializeRecyclerViews()
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


    private fun changeProgressBarVisibility(loading: Boolean) {
        binding.progressBar.isVisible = loading
    }

    private fun initializeRecyclerViews() {
        trackRecycler = findViewById(R.id.trackRecycler)
        trackRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackAdapter = TrackAdapter(this, songs, tracksInteractor.getSearchHistory())
        searchHistoryView = findViewById(R.id.searchHistoryView)
        cleanHistoryButton = findViewById(R.id.cleanHistoryButton)

        historyRecycler = findViewById(R.id.searchHistoryRecycler)
        historyRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchHistoryAdapter =
            TrackAdapter(this, historyTracks, tracksInteractor.getSearchHistory())

        progressBar = findViewById(R.id.progressBar)

        viewModel.loadTracksHistory()

        historyRecycler.adapter = searchHistoryAdapter

        //СЭТ ФОКУС ИНПУТА
        setOnFocusUserInput()
        viewModel.setHistorySharedPrefListener()
        setOnClickCleanHistoryButton()
        setTextWatcher()
    }

    private fun setTextWatcher() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    deleteButton.visibility = View.GONE
                    val inputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    inputMethodManager?.hideSoftInputFromWindow(inputEditText.windowToken, 0)
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
                //
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun setOnClickCleanHistoryButton() {
        cleanHistoryButton.setOnClickListener {
            viewModel.cleanHistory()
            //searchHistoryHandler.cleanHistory()
            searchHistoryAdapter.notifyDataSetChanged()
            searchHistoryView.visibility = View.GONE

        }
    }

    private fun setOnFocusUserInput() {
        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            statusView.visibility = View.GONE
            showTracksHistory(hasFocus)
        }
    }

    private fun showTracksHistory(hasFocus: Boolean) {
        // viewModel.loadTracksHistory()
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
        inputEditText = findViewById(R.id.search_edit_text)
        deleteButton = findViewById(R.id.delete_button)
        cleanHistoryButton = findViewById(R.id.cleanHistoryButton)
        statusView = findViewById(R.id.searchStatusLayout)
        inputEditText.doOnLayout { inputEditText.hint }
        deleteButton.setOnClickListener {
            inputEditText.setText("")
        }
        if (!inputSearchText.isNullOrEmpty()) {
            inputEditText.setText(inputSearchText)
        }

    }

    private fun showStatusView(typeOfMessage: String, inputUserText: String) {
        statusImage = findViewById(R.id.searchStatusImage)
        statusText = findViewById(R.id.searchStatusText)
        updateButton = findViewById(R.id.updateButton)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(inputSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputSearchText = savedInstanceState.getString(inputSearchText)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeLoadingObserver()
    }
}

private fun Bundle.putString(inputSearchText: String?) {
}









