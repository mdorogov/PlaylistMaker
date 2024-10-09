package com.practicum.playlistmaker.search.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.practicum.playlistmaker.search.data.db.entity.TrackEntity

@Dao
interface TrackDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTrackEntity(track: TrackEntity)

    @Query("SELECT * FROM favorite_tracks")
    fun getFavTracks(): List<TrackEntity>

    @Delete(entity = TrackEntity::class)
    fun deleteTrackEntity(trackEntity: TrackEntity)

    @Query("SELECT * FROM favorite_tracks WHERE trackId = :trackId")
    fun isThereTrack(trackId : Int) : Int

}