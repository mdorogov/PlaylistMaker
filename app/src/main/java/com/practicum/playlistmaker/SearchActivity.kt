package com.practicum.playlistmaker

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.view.doOnLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    var inputSearchText: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            val backButtonIntent = Intent(this, MainActivity::class.java)
            startActivity(backButtonIntent)
        }
        val inputEditText = findViewById<EditText>(R.id.search_edit_text)
        val deleteButton = findViewById<ImageView>(R.id.delete_button)
        inputEditText.doOnLayout { inputEditText.hint }
        deleteButton.setOnClickListener {
            inputEditText.setText("")
        }
        if (!inputSearchText.isNullOrEmpty()) {
            inputEditText.setText(inputSearchText)
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
                } else {
                    deleteButton.visibility = View.VISIBLE
                    inputEditText.requestFocus()
                    inputSearchText = s.toString()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)

        createTrackViewer()
    }

    private fun createTrackViewer() {
        val trackRecycler = findViewById<RecyclerView>(R.id.trackRecycler)
        trackRecycler.layoutManager = LinearLayoutManager(this)
        val trackAdapter = TrackAdapter(createTracks())
        trackRecycler.adapter = trackAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(inputSearchText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        inputSearchText = savedInstanceState.getString(inputSearchText)

    }


    private fun createTracks(): List<Track> {

        val track1 = Track(
            "Smells Like Teen Spirit", "Nirvana", "5:01",
            getString(R.string.track1_url)
        )
        val track2 = Track(
            "Billie Jean", "Michael Jackson", "4:35",
            getString(R.string.track2_url)
        )
        val track3 = Track(
            "Stayin' Alive", "Bee Gees", "4:10",
            getString(R.string.track3_url)
        )
        val track4 = Track(
            "Whole Lotta Love", "Led Zeppelin", "5:33",
            getString(R.string.track4_url)
        )
        val track5 = Track(
            "Sweet Child O'Mine", "Guns N' Roses", "5:03",
            getString(R.string.track5_url)
        )

        val list: List<Track> = listOf(track1, track2, track3, track4, track5)
        return list
    }
}

private fun Bundle.putString(inputSearchText: String?) {

}







