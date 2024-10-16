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

    @Query("SELECT * FROM favorite_tracks ORDER BY timeStamp desc")
    fun getFavTracks(): List<TrackEntity>

    @Delete(entity = TrackEntity::class)
    fun deleteTrackEntity(trackEntity: TrackEntity)

    @Query("SELECT * FROM favorite_tracks WHERE track_Id = :trackId")
    fun isThereTrack(trackId : String) : Int

}