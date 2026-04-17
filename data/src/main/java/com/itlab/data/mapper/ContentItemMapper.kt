package com.itlab.data.mapper

import com.itlab.data.model.ContentItemDto
import com.itlab.data.model.DataSourceDto
import com.itlab.data.model.TextFormatDto
import com.itlab.domain.model.ContentItem
import com.itlab.domain.model.DataSource
import com.itlab.domain.model.TextFormat

fun ContentItem.toDto(): ContentItemDto =
    when (this) {
        is ContentItem.Text -> ContentItemDto.Text(text, format.toDto())
        is ContentItem.Image -> ContentItemDto.Image(source.toDto(), mimeType, width, height)
        is ContentItem.File -> ContentItemDto.File(source.toDto(), mimeType, name, size)
        is ContentItem.Link -> ContentItemDto.Link(url, title)
    }

fun ContentItemDto.toDomain(): ContentItem =
    when (this) {
        is ContentItemDto.Text -> ContentItem.Text(text, format.toDomain())
        is ContentItemDto.Image -> ContentItem.Image(source.toDomain(), mimeType, width, height)
        is ContentItemDto.File -> ContentItem.File(source.toDomain(), mimeType, name, size)
        is ContentItemDto.Link -> ContentItem.Link(url, title)
    }

fun DataSource.toDto() = DataSourceDto(localPath, remoteUrl)

fun DataSourceDto.toDomain() = DataSource(localPath, remoteUrl)

fun TextFormat.toDto() = TextFormatDto.valueOf(this.name)

fun TextFormatDto.toDomain() = TextFormat.valueOf(this.name)
