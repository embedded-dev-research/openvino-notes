package com.itlab.data.db

import androidx.room.TypeConverter
import kotlinx.datetime.Instant

class DataTimeConverers {
    @TypeConverter
    fun fromTimestamp(value: Long?): Instant? {
        return value?.let {
            Instant.fromEpochMilliseconds(it)
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Instant?): Long? {
        return date?.toEpochMilliseconds()
    }
}
