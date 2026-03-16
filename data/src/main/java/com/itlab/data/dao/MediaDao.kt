package com.itlab.data.entity

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaDao{
    @Query("SELECT * FROM media WHERE noteId = :noteId")
    fun getMediaForNote(noteId: String): Flow<List<MediaEntity>>

    @Insert
    suspend fun insert(media: MediaEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mediaList: List<MediaEntity>)

    @Delete
    suspend fun delete(media: MediaEntity)
}
