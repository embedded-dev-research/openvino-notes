package com.itlab.domain.cloud

import kotlinx.datetime.Instant
import java.io.File

interface CloudDataSource {
    suspend fun listNoteMetadata(userId: String): Result<List<CloudNoteMetadata>>

    suspend fun downloadNote(key: String): Result<String>
    suspend fun uploadNote(key: String, json: String): Result<Unit>
    suspend fun deleteNote(key: String): Result<Unit>

    suspend fun uploadMedia(key: String, file: File): Result<Unit>
    suspend fun downloadMedia(key: String, destination: File): Result<Unit>
    suspend fun deleteMedia(key: String): Result<Unit>
}

data class CloudNoteMetadata(
    val key: String,
    val updatedAt: Instant
)
