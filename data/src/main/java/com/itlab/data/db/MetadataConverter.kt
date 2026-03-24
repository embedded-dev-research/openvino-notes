package com.itlab.data.db

import androidx.room.TypeConverter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MetadataConverter {
    @TypeConverter
    fun fromMetadata(metadata: Map<String, String>): String = Json.encodeToString(metadata)

    @TypeConverter
    fun toMetadata(metadataString: String): Map<String, String> =
        try {
            Json.decodeFromString(metadataString)
        } catch (e: Exception) {
            emptyMap()
        }
}
