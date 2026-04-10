package com.itlab.domain.usecase

import com.itlab.domain.model.Note
import com.itlab.domain.repository.NotesRepository
import kotlinx.datetime.Clock
import java.util.UUID

class CreateNoteUseCase(
    private val repo: NotesRepository,
) {
    suspend operator fun invoke(note: Note): String {
        val now = Clock.System.now()
        val note =
            note.copy(
                id = note.id ?: UUID.randomUUID().toString(),
                createdAt = note.createdAt ?: now,
                updatedAt = now,
            )
        return repo.createNote(note)
    }
}
