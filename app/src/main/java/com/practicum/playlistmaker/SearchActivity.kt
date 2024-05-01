package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


const val SEARCH_TRACK_HISTORY = "search_track_history"
const val JSON_HISTORY_KEY = "key_for_json_history"

class SearchActivity : AppCompatActivity() {
    var inputSearchText: String? = null

    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val itunesService = retrofit.create(ItunesApi::class.java)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        searchTrackHistory = getSharedPreferences(SEARCH_TRACK_HISTORY, MODE_PRIVATE)
        searchHistoryHandler = SearchHistory(searchTrackHistory)

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
        inputEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                trackSearching(inputEditText.text.toString())
            }
            false
        }
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
                    trackSearching(inputUserText)
                }
                updateButton.visibility = View.VISIBLE
            }
        }
        trackRecycler.visibility = View.GONE
        statusView.visibility = View.VISIBLE

    }

    private fun trackSearching(userRequest: String) {

        if (userRequest.isNotEmpty()) {
            itunesService.search(userRequest).enqueue(object : Callback<ItunesResponse> {
                override fun onResponse(
                    call: Call<ItunesResponse>,
                    response: Response<ItunesResponse>,
                ) {
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
                            showStatusView("Not Found", userRequest)
                        }
                    }
                }

                override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                    showStatusView("No Connection", userRequest)
                }
            })
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









