package com.practicum.playlistmaker.library.ui.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.ContactsContract.Data
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.animation.AnimationUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistCreatingBinding
import com.practicum.playlistmaker.library.ui.state.PlaylistCreatingState
import com.practicum.playlistmaker.library.ui.view_model.FavoriteTracksViewModel
import com.practicum.playlistmaker.library.ui.view_model.PlaylistCreatingViewModel
import com.practicum.playlistmaker.search.mapper.DimensConverter
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileOutputStream
import java.sql.Time
import java.sql.Timestamp

open class PlaylistCreatingFragment : Fragment() {

    companion object {
        fun newInstance() = PlaylistCreatingFragment().apply {
            arguments = Bundle().apply { }
        }
    }

    open val viewModel: PlaylistCreatingViewModel by viewModel {
        parametersOf()
    }

    protected lateinit var playlistArtwork: ImageView
    protected lateinit var playlistNameEditText: TextInputLayout
    protected lateinit var playlistDescriptionEditText: TextInputLayout
    protected lateinit var createPlaylistButton: Button

    private lateinit var artworkUri: Uri
    protected var isArtworkChosen: Boolean = false

    var isPlaylistNameEmpty = true

    private var _binding: FragmentPlaylistCreatingBinding? = null
    private val binding get() = _binding!!


    var isNewPlaylistDataFilled: Boolean = false

    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Glide.with(playlistArtwork)
                .load(uri)
                .placeholder(R.drawable.placeholder)
                .apply(
                    RequestOptions().transform(
                        MultiTransformation(
                            CenterCrop(),
                            RoundedCorners(DimensConverter.dpToPx(8f, playlistArtwork))
                        )
                    )
                )
                .into(playlistArtwork)
            artworkUri = uri
            isArtworkChosen = true
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistCreatingBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    openExitDialog()
                }
            })

        setPlaylistCreatingViews()

    }


    private fun render(state: PlaylistCreatingState) {
        when (state) {
            is PlaylistCreatingState.PlaylistCreated -> showToastAfterPlaylistCreating(state.playlistName)
            else -> {}
        }
    }

    private fun setPlaylistCreatingViews() {
        playlistArtwork = binding.playlistArtwork
        playlistNameEditText = this.binding.playlistNameEdit
        playlistDescriptionEditText = this.binding.playlistDescriptionEdit
        createPlaylistButton = this.binding.createPlaylistButton

        binding.playlistCreatingFragment.animation =
            android.view.animation.AnimationUtils.loadAnimation(
                requireContext(),
                R.anim.scr_animation
            )
        binding.playlistCreatingFragment.animate()

        binding.backButton.setOnClickListener {
            openExitDialog()
            if (isNewPlaylistDataFilled) {
                openExitDialog()
            }
        }

        playlistArtwork.setOnClickListener {
            choosePlaylistArtwork()
        }

        binding.createPlaylistButton.setOnClickListener {
            if (!isPlaylistNameEmpty) {
                createPlaylist()
                saveChosenArtwork()
            }
        }

        setTextWatcher()
    }


    open fun openExitDialog() {
        if (!isPlaylistNameEmpty) {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")
                .setNegativeButton("Отмена") { dialog, which ->

                }
                .setPositiveButton("Завершить") { dialog, which ->
                    finish()
                }
                .show()
        } else {
            finish()
        }
    }

    private fun finish() {
        parentFragmentManager.popBackStack()
    }

    fun setTextWatcher() {
        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    isPlaylistNameEmpty = true
                    binding.createPlaylistButton.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.gray
                        )
                    )
                } else {
                    isPlaylistNameEmpty = false
                    binding.createPlaylistButton.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.blue
                        )
                    )
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        }
        binding.playlistNameEditTextCreating.addTextChangedListener(simpleTextWatcher)
        binding.playlistNameEditTextCreating.setSelectAllOnFocus(true)
        binding.playlistDescriptionEditCreating.setSelectAllOnFocus(true)
    }

    open fun saveChosenArtwork(): String {

        if (isArtworkChosen) {
            val filePath = File(
                requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                "artworks"
            )

            if (!filePath.exists()) {
                filePath.mkdirs()
            }

            val file = File(filePath, "art_" + System.currentTimeMillis().toString())
            val inputStream = requireActivity().contentResolver.openInputStream(artworkUri)
            val outputStream = FileOutputStream(file)
            BitmapFactory.decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

            return file.absolutePath
        } else {
            return "android.resource://com.practicum.playlistmaker/drawable/artwork_placeholder"
        }

    }

    open fun createPlaylist() {
        viewModel.createPlaylist(
            saveChosenArtwork(),
            binding.playlistNameEditTextCreating.text.toString(),
            binding.playlistDescriptionEditCreating.text?.toString()
        )
    }

    private fun choosePlaylistArtwork() {
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

    }


    fun showToastAfterPlaylistCreating(playlistName: String) {
        Toast.makeText(requireContext(), "Плейлист " + playlistName + " создан", Toast.LENGTH_SHORT)
            .show()
        finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}