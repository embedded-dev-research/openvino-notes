package com.itlab.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

@Entity(tableName = "folders")
data class FolderEntity(
    @PrimaryKey val id: String,
    val name: String,
    val createdAt: Instant,
    val updatedAt: Instant,
    val isSynced: Boolean = false,
    val isDeleted: Boolean = false,
    val metadata: Map<String, String>,
)
