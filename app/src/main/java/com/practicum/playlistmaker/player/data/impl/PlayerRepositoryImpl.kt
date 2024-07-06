package com.practicum.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.data.api.PlayerRepository

class PlayerRepositoryImpl() : PlayerRepository {
    override fun getPlayer(): MediaPlayer {
        return MediaPlayer()
    }
}