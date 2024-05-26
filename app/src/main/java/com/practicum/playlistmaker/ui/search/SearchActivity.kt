package com.practicum.playlistmaker.ui.search

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.data.dto.ItunesResponse
import com.practicum.playlistmaker.MainActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.data.SearchHistory
import com.practicum.playlistmaker.data.models.Track
import com.practicum.playlistmaker.creator.Creator
import com.practicum.playlistmaker.domain.api.TracksInteractor
import com.practicum.playlistmaker.domain.api.TracksInteractor.TracksConsumer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


const val SEARCH_TRACK_HISTORY = "search_track_history"
const val JSON_HISTORY_KEY = "key_for_json_history"
const val AUTO_SEARCHING_DELAY = 2000L

class SearchActivity : AppCompatActivity() {
    var inputSearchText: String? = null

    private val itunesBaseUrl = "https://itunes.apple.com"
    /*private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val itunesService = retrofit.create(ItunesApi::class.java)*/
    private lateinit var inputEditText: EditText
    private lateinit var trackRecycler: RecyclerView
    private lateinit var deleteButton: ImageView
    private lateinit var statusView: LinearLayout
    private lateinit var statusImage: ImageView
    private lateinit var statusText: TextView
    private lateinit var updateButton: Button

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchTrackHistory: SharedPreferences
    private lateinit var searchHistoryView: LinearLayout
    private lateinit var historyRecycler: RecyclerView
    private lateinit var cleanHistoryButton: Button

    private var songs = ArrayList<Track>()

   private lateinit var searchHistoryHandler: SearchHistory
    private lateinit var searchHistoryAdapter: TrackAdapter

    private lateinit var progressBar: ProgressBar
    private lateinit var searchRunnable: Runnable
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var getTracksInteractor: TracksInteractor


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        getTracksInteractor =  Creator.provideTracksInteractor()

        /*searchTrackHistory = getSharedPreferences(SEARCH_TRACK_HISTORY, MODE_PRIVATE)*/
        searchHistoryHandler = getTracksInteractor.getSearchHistory()


        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            val backButtonIntent = Intent(this, MainActivity::class.java)
            startActivity(backButtonIntent)
        }

        initializeViews()
        initializeRecyclerViews()
    }

    private fun initializeRecyclerViews() {
        trackRecycler = findViewById(R.id.trackRecycler)
        trackRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackAdapter = TrackAdapter(this, songs, searchHistoryHandler)
        searchHistoryView = findViewById(R.id.searchHistoryView)
        cleanHistoryButton = findViewById(R.id.cleanHistoryButton)
        historyRecycler = findViewById(R.id.searchHistoryRecycler)
        historyRecycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        searchHistoryAdapter = TrackAdapter(this, searchHistoryHandler.array, searchHistoryHandler)

        progressBar = findViewById(R.id.progressBar)
        searchRunnable = Runnable { trackSearching() }

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            searchHistoryView.visibility = if (hasFocus && inputEditText.text.isEmpty()) {
                if (searchHistoryHandler.array.isNotEmpty()) {
                    historyRecycler.adapter = searchHistoryAdapter
                    searchHistoryAdapter.notifyDataSetChanged()
                    View.VISIBLE
                } else View.GONE
            } else View.GONE
        }

        var sharedListener = OnSharedPreferenceChangeListener { sharedPreferences, key ->
            if (key == JSON_HISTORY_KEY) {
                val history = sharedPreferences?.getString(JSON_HISTORY_KEY, null)
                if (history != null) {

                    historyRecycler.adapter = searchHistoryAdapter
                    searchHistoryAdapter.notifyDataSetChanged()
                }
            }
        }
        searchTrackHistory.registerOnSharedPreferenceChangeListener(sharedListener)

        cleanHistoryButton.setOnClickListener {
            searchHistoryHandler.cleanHistory()
            searchHistoryAdapter.notifyDataSetChanged()
            searchHistoryView.visibility = View.GONE

        }


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

                    searchHistoryView.visibility =
                        if (inputEditText.hasFocus() && searchHistoryHandler.array.isNotEmpty() == true) View.VISIBLE else View.GONE
                    searchHistoryAdapter.notifyDataSetChanged()
                    inputEditText.clearFocus()
                } else {
                    searchHistoryView.visibility = View.GONE
                    deleteButton.visibility = View.VISIBLE
                    inputEditText.requestFocus()
                    inputSearchText = s.toString()
                    trackRecycler.visibility = View.GONE
                    autoTrackSearching()

                    deleteButton.setOnClickListener {
                        statusView.visibility = View.GONE
                        inputEditText.setText("")
                        songs.clear()
                        trackAdapter.notifyDataSetChanged()
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
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

    private fun autoTrackSearching() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, AUTO_SEARCHING_DELAY)
    }

   /* private fun trackSearching() {
        progressBar.visibility = View.VISIBLE
        var userRequest = inputEditText.text.toString()


        if (userRequest.isNotEmpty()) {
            itunesService.search(userRequest).enqueue(object : Callback<ItunesResponse> {
                override fun onResponse(

                    call: Call<ItunesResponse>,
                    response: Response<ItunesResponse>,
                ) {
                    progressBar.visibility = View.GONE
                    if (response.code() == 200) {
                        songs.clear()
                        if (response.body()?.results?.isNotEmpty() == true) {
                            trackRecycler.visibility = View.VISIBLE
                            statusView.visibility = View.GONE
                            songs.addAll(response.body()?.results!!)
                            trackRecycler.adapter = trackAdapter
                            trackAdapter.notifyDataSetChanged()
                        }
                        if (songs.isEmpty()) {
                            progressBar.visibility = View.GONE
                            showStatusView("Not Found", userRequest)
                        }
                    }
                }

                override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    showStatusView("No Connection", userRequest)
                }
            })
        }
    }
*/

    private fun makeRequest(userRequest: String) {
        progressBar.visibility = View.GONE
try{
    var resp = getTracksInteractor.searchTracks(userRequest, consumer = object : TracksConsumer {

        override fun consume(foundTracks: List<Track>) {
            handler.post{
                if(foundTracks.isNotEmpty()){
                    songs.clear()
                    trackRecycler.visibility = View.VISIBLE
                    statusView.visibility = View.GONE
                    songs.addAll(foundTracks)
                    trackRecycler.adapter = trackAdapter
                    trackAdapter.notifyDataSetChanged()
                } else {
                    progressBar.visibility = View.GONE
                    showStatusView("Not Found", userRequest)
                }
            }

        }

    })
} catch (ex: Exception){
    showStatusView("No Connection", userRequest)
}
    }

    private fun trackSearching() {
        progressBar.visibility = View.VISIBLE
        var userRequest = inputEditText.text.toString()

        if (userRequest.isNotEmpty()) {
            makeRequest(userRequest)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(inputSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputSearchText = savedInstanceState.getString(inputSearchText)

    }
}

private fun Bundle.putString(inputSearchText: String?) {
}









