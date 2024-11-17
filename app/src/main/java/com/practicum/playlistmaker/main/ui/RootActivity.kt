package com.practicum.playlistmaker.main.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityRootBinding
import com.practicum.playlistmaker.library.ui.fragments.FavoriteTracksFragment
import com.practicum.playlistmaker.library.ui.fragments.LibraryFragment
import com.practicum.playlistmaker.library.ui.fragments.PlaylistsFragment
import com.practicum.playlistmaker.search.ui.fragment.SearchFragment
import com.practicum.playlistmaker.settings.ui.fragment.SettingsFragment

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController



        binding.bottomNavigationView.setupWithNavController(navController)
    }

    fun setBottomNavigationView(isShown: Boolean) {
        if (isShown) {
            binding.bottomNavigationView.visibility = View.VISIBLE
        } else binding.bottomNavigationView.visibility = View.GONE
    }

}
