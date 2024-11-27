package com.practicum.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.library.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.library.data.db.entity.SavedTrackEntity

@Dao
interface SavedTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrack(savedTrack: SavedTrackEntity)

    @Query("SELECT * FROM saved_tracks")
    suspend fun getAllSavedTracks(): List<SavedTrackEntity>

    @Query("SELECT * FROM saved_tracks WHERE track_id = :trackId")
    suspend fun getSavedTrack(trackId: String): SavedTrackEntity

    @Delete(entity = SavedTrackEntity::class)
    suspend fun deleteSavedTrackEntity(savedTrackEntity: SavedTrackEntity)

    @Query("SELECT * FROM saved_tracks WHERE track_id IN (:savedIDs)")
    fun getSavedTracks(savedIDs: List<Int>): List<SavedTrackEntity>

    @Query("DELETE FROM saved_tracks WHERE track_id = :trackId")
    suspend fun deleteSavedTrackById(trackId: Int)
}