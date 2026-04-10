package com.itlab.domain.model

import kotlinx.datetime.Instant

data class NoteFolder(
    val id: String? = null,
    val name: String,
    val createdAt: Instant? = null,
    val updatedAt: Instant? = null,
    val metadata: Map<String, String> = emptyMap(),
)
