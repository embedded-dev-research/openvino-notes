package com.itlab.data.mapper

import com.itlab.data.entity.MediaEntity
import com.itlab.data.entity.NoteEntity
import com.itlab.domain.model.ContentItem
import com.itlab.domain.model.DataSource
import com.itlab.domain.model.Note
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

class NoteMapper(
    private val json: Json =
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        },
) {
    fun toEntities(note: Note): Pair<NoteEntity, List<MediaEntity>> {
        val noteId = note.id

        val mediaEntities =
            note.contentItems.mapNotNull { item ->
                toMediaEntity(item, noteId)
            }

        val noteEntity =
            NoteEntity(
                id = noteId,
                title = note.title,
                folderId = note.folderId,
                content = json.encodeToString(note.contentItems),
                createdAt = note.createdAt,
                updatedAt = note.updatedAt,
                tags = json.encodeToString(note.tags),
                isFavorite = note.isFavorite,
                isSynced = false,
            )

        return noteEntity to mediaEntities
    }

    fun toDomain(entity: NoteEntity): Note {
        val items =
            try {
                json.decodeFromString<List<ContentItem>>(entity.content)
            } catch (e: Exception) {
                emptyList()
            }

        val tags =
            try {
                json.decodeFromString<Set<String>>(entity.tags ?: "[]")
            } catch (e: Exception) {
                emptySet()
            }

        return Note(
            id = entity.id,
            title = entity.title,
            contentItems = items,
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt,
            tags = tags,
            isFavorite = entity.isFavorite,
        )
    }

    private fun toMediaEntity(
        item: ContentItem,
        noteId: String,
    ): MediaEntity? =
        when (item) {
            is ContentItem.Image -> {
                MediaEntity(
                    id = UUID.randomUUID().toString(),
                    noteId = noteId,
                    type = "IMAGE",
                    remoteUrl = (item.source as? DataSource.Remote)?.url ?: "",
                    localPath = (item.source as? DataSource.Local)?.path,
                    mimeType = item.mimeType,
                )
            }

            is ContentItem.File -> {
                MediaEntity(
                    id = UUID.randomUUID().toString(),
                    noteId = noteId,
                    type = "FILE",
                    remoteUrl = (item.source as? DataSource.Remote)?.url ?: "",
                    localPath = (item.source as? DataSource.Local)?.path,
                    mimeType = item.mimeType,
                    size = item.size,
                )
            }

            else -> {
                null
            }
        }
}
