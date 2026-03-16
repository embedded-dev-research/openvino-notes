package com.itlab.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

@Entity(
    tableName = "media",
    foreignKeys  = [ForeignKey(
        entity = NoteEntity::class,
        parentColumns = ["id"],
        childColumns = ["noteId"],
        onDelete = ForeignKey.CASCADE
    )]
)

data class MediaEntity(
    @PrimaryKey
    val id : String,
    val noteId : String,
    val type : String,
    val remoteUrl : String,
    val localPath : String?,
    val mimeType : String,
    val size : Long? = null
)