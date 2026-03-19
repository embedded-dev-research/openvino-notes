package com.itlab.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.itlab.data.entity.MediaEntity
import com.itlab.data.entity.NoteEntity

@Database(
    entities = [NoteEntity::class, MediaEntity::class],
    version = 1,
    exportShema = false,
)
@TypeConverters(DataTimeConverters::class)
abstract class AppDatabase : RoomDatabase(){
    
}
