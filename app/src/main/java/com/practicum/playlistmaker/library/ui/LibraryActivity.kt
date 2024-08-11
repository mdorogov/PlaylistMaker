package com.practicum.playlistmaker.library.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.practicum.playlistmaker.R

class LibraryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_library)
    }
}