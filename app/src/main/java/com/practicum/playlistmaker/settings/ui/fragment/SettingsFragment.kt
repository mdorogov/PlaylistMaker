package com.practicum.playlistmaker.settings.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment(){

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SettingsViewModel>()
    private lateinit var themeSwitcher: SwitchMaterial

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
   return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        themeSwitcher = binding.themeSwitcher
        val shareButton = binding.shareButton
        val supportButton = binding.supportButton
        val agreementButton = binding.agreementButton


        viewModel.getSettingsState().observe(viewLifecycleOwner){settingsState ->
            themeSwitcher.isChecked = settingsState.isDarkModeOn
            changeThemeMode(settingsState.isDarkModeOn)
        }




        themeSwitcher.setOnCheckedChangeListener{switcher, checked ->

            viewModel.saveThemePreference(checked)
        }


        shareButton.setOnClickListener{
            viewModel.shareApp()
        }


        supportButton.setOnClickListener{
            viewModel.sendEmail()
        }


        agreementButton.setOnClickListener{
            viewModel.showAgreement()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun changeThemeMode(checked: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (checked){
                AppCompatDelegate.MODE_NIGHT_YES

            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}