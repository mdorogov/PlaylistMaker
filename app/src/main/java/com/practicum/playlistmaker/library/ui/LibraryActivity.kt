package com.practicum.playlistmaker.library.ui

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentLibraryBinding
import com.practicum.playlistmaker.library.ui.fragments.LibraryPagerAdapter

class LibraryActivity : AppCompatActivity() {

    private lateinit var binding: FragmentLibraryBinding
    private lateinit var tabMediator: TabLayoutMediator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            finish()
        }

        binding.libViewPager.adapter = LibraryPagerAdapter(supportFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.libTabLayout, binding.libViewPager){tab, position ->
            when(position) {
                0 -> tab.text = "Избранные треки"
                1 -> tab.text = "Плейлисты"
            }
        }
        tabMediator.attach()
//        if (savedInstanceState == null) {
//            supportFragmentManager.commit {
//                add<FavoriteTracksFragment>(R.id.fragment_container_library)
//            }
        }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
    }
