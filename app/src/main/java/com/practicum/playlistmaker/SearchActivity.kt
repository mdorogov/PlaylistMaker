package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
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
import android.widget.Toast
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    private val songs = ArrayList<Track>()
    private val trackAdapter = TrackAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            val backButtonIntent = Intent(this, MainActivity::class.java)
            startActivity(backButtonIntent)
        }

        inputEditText = findViewById(R.id.search_edit_text)
        deleteButton = findViewById(R.id.delete_button)
        statusView = findViewById<LinearLayout?>(R.id.searchStatusLayout)
        inputEditText.doOnLayout { inputEditText.hint }
        deleteButton.setOnClickListener {
            inputEditText.setText("")
        }
        if (!inputSearchText.isNullOrEmpty()) {
            inputEditText.setText(inputSearchText)
        }

        trackAdapter.tracks = songs
        trackRecycler = findViewById(R.id.trackRecycler)
        trackRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        trackRecycler.adapter = trackAdapter


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
                } else {
                    deleteButton.visibility = View.VISIBLE
                    inputEditText.requestFocus()
                    inputSearchText = s.toString()
                    deleteButton.setOnClickListener {
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

    private fun showStatusView(typeOfMessage: String, inputUserText: String) {

        statusImage = findViewById(R.id.searchStatusImage)
        statusText = findViewById(R.id.searchStatusText)
        updateButton = findViewById(R.id.updateButton)

        when (typeOfMessage) {
            "Not Found" -> {
                statusImage.setImageResource(R.drawable.not_found_error)
                statusText.setText(getString(R.string.not_found_text_ru))
            }

            "No Connection" -> {
                statusImage.setImageResource(R.drawable.no_connection)
                statusText.setText(getString(R.string.no_connection_text_ru))
            }
        }
        trackRecycler.visibility = View.GONE
        statusView.visibility = View.VISIBLE
        updateButton.setOnClickListener {
            trackSearching(inputUserText)
        }
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
                            trackAdapter.notifyDataSetChanged()
                        }
                        if (songs.isEmpty()) {
                            showStatusView("Not Found", userRequest)
                        }
                    }
                }

                override fun onFailure(call: Call<ItunesResponse>, t: Throwable) {
                    showStatusView("No Connection", userRequest)
                    Toast.makeText(this@SearchActivity, "хреново", Toast.LENGTH_SHORT).show()
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









