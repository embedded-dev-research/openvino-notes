package com.itlab.data.mapper

import com.itlab.data.entity.NoteEntity
import com.itlab.data.model.NoteBodyDto
import com.itlab.data.model.NoteDto
import com.itlab.data.model.NoteMetaDto
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class NoteEntityJsonConverter(
    private val json: Json =
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        },
) {
    fun NoteEntity.toDto(): NoteDto =
        NoteDto(
            id = id,
            folderId = folderId,
            body =
                NoteBodyDto(
                    title = title,
                    content = content,
                    summary = summary,
                ),
            metadata =
                NoteMetaDto(
                    createdAt = createdAt.toEpochMilliseconds(),
                    updatedAt = updatedAt.toEpochMilliseconds(),
                    tags = tags,
                    isFavorite = isFavorite,
                ),
        )

    fun NoteEntity.toJson(): String {
        val dto = this.toDto()
        return json.encodeToString(dto)
    }

    fun toEntity(
        jsonString: String,
        userId: String,
    ): NoteEntity {
        val dto = json.decodeFromString<NoteDto>(jsonString)
        return dto.toEntity(userId)
    }

    fun NoteDto.toEntity(userId: String): NoteEntity =
        NoteEntity(
            id = id,
            userId = userId,
            title = body.title,
            content = body.content,
            folderId = folderId,
            createdAt = kotlinx.datetime.Instant.fromEpochMilliseconds(metadata.createdAt),
            updatedAt = kotlinx.datetime.Instant.fromEpochMilliseconds(metadata.updatedAt),
            tags = metadata.tags,
            isFavorite = metadata.isFavorite,
            isSynced = true,
            isDeleted = false,
            summary = body.summary,
        )
}
