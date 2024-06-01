package com.practicum.playlistmaker.data.api

import android.media.MediaPlayer

interface PlayerRepository {

fun getPlayer(): MediaPlayer
}