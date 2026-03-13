package com.itlab.domain.model

import java.util.Date
import java.util.UUID

data class Note(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val contentItems: List<ContentItem> = emptyList(),
    val createdAt : Date = Date(),
    val updatedAt : Date = Date(),
    val tags: Set<String> = emptySet(),
    val isFavorite : Boolean = false
)

sealed class ImageSource {
    data class Local(val path: String) : ImageSource()
    data class Remote(val url: String) : ImageSource()
}

sealed class ContentItem {
    data class Text(
        val text : String,
        val format : TextFormat = TextFormat.PLAIN
    ) : ContentItem()

    data class Image(
        val source: ImageSource,
        val width : Int? = null,
        val height : Int? = null
    ) : ContentItem()

    data class File(
        val uri : String, 
        val mimeType : String,
        val name : String,
        val size : Long? = null
    ) : ContentItem()

    data class Link(
        val url : String,
        val title : String? = null
    ) : ContentItem()
}

enum class TextFormat {
    PLAIN,
    MARKDOWN,
    HTML
}
