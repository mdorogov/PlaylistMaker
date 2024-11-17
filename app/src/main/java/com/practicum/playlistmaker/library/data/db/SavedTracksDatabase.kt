package com.practicum.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.library.data.db.dao.PlaylistDao
import com.practicum.playlistmaker.library.data.db.dao.SavedTrackDao
import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.library.data.db.entity.SavedTrackEntity

@Database(version = 1, entities = [SavedTrackEntity::class])
abstract class SavedTracksDatabase : RoomDatabase() {
    abstract fun savedTrackDao(): SavedTrackDao

}