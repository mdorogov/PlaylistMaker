package com.practicum.playlistmaker.player.data.api

import android.media.MediaPlayer

interface PlayerRepository {

fun getPlayer(): MediaPlayer
}