package com.itlab.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ContentItemDto {
    @Serializable
    data class Text(
        val text: String,
        val format: TextFormatDto = TextFormatDto.PLAIN,
    ) : ContentItemDto()

    @Serializable
    data class Image(
        val source: DataSourceDto,
        val mimeType: String,
        val width: Int? = null,
        val height: Int? = null,
    ) : ContentItemDto()

    @Serializable
    data class File(
        val source: DataSourceDto,
        val mimeType: String,
        val name: String,
        val size: Long? = null,
    ) : ContentItemDto()

    @Serializable
    data class Link(
        val url: String,
        val title: String? = null,
    ) : ContentItemDto()
}

@Serializable
enum class TextFormatDto {
    @SerialName("PLAIN")
    PLAIN,

    @SerialName("MARKDOWN")
    MARKDOWN,

    @SerialName("HTML")
    HTML,
}

@Serializable
data class DataSourceDto(
    val localPath: String? = null,
    val remoteUrl: String? = null,
)
