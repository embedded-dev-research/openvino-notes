package com.itlab.domain.usecase
import com.itlab.domain.model.Note
import com.itlab.domain.repository.NotesRepository
import com.itlab.domain.repository.NoteFolderRepository
import kotlinx.datetime.Clock

class MoveNoteToFolderUseCase(
    private val notesRepo: NotesRepository,
    private val folderRepo: NoteFolderRepository
) {
    suspend operator fun invoke(folderId: String, noteId: String) {
        val folder = folderRepo.getFolderById(folderId) ?: throw IllegalArgumentException("Folder not found: $folderId")
        val note = notesRepo.getNoteById(noteId) ?: throw IllegalArgumentException("Note not found: $noteId")
        val updated = note.copy(folderId = folderId, updatedAt = Clock.System.now())
        notesRepo.updateNote(updated)
    }
}
