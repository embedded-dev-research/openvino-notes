package com.itlab.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import java.util.Collections.emptyList
import java.util.Collections.emptySet
import java.util.UUID

data class Note(
    val userId: String,
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val folderId: String? = null,
    val contentItems: List<ContentItem> = emptyList(),
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
    val tags: Set<String> = emptySet(),
    val isFavorite: Boolean = false,
    val summary: String? = null,
    val syncStatus: SyncStatus = SyncStatus.PENDING,
)

data class DataSource(
    val localPath: String? = null,
    val remoteUrl: String? = null,
) {
    val displayPath: String? get() = localPath ?: remoteUrl
}

sealed class ContentItem {
    data class Text(
        val text: String,
        val format: TextFormat = TextFormat.PLAIN,
    ) : ContentItem()

    data class Image(
        val source: DataSource,
        val mimeType: String,
        val width: Int? = null,
        val height: Int? = null,
    ) : ContentItem()

    data class File(
        val source: DataSource,
        val mimeType: String,
        val name: String,
        val size: Long? = null,
    ) : ContentItem()

    data class Link(
        val url: String,
        val title: String? = null,
    ) : ContentItem()
}

enum class TextFormat {
    PLAIN,
    MARKDOWN,
    HTML,
}

enum class SyncStatus {
    SYNCED,
    PENDING,
    SYNCING,
    ERROR,
}
