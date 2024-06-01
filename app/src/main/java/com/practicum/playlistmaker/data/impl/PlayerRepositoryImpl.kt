package com.practicum.playlistmaker.data.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.api.PlayerRepository

class PlayerRepositoryImpl() : PlayerRepository {
    override fun getPlayer(): MediaPlayer {
        return MediaPlayer()
    }
}