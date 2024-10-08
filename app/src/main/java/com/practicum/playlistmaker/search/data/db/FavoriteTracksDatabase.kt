package com.practicum.playlistmaker.search.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.search.data.db.dao.TrackDao
import com.practicum.playlistmaker.search.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class FavoriteTracksDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao
}