package com.itlab.domain.usecase.folderusecase

import com.itlab.domain.model.NoteFolder
import com.itlab.domain.repository.NoteFolderRepository
import com.itlab.domain.usecase.requireNotBlank
import kotlin.time.Clock

class UpdateFolderUseCase(
    private val repo: NoteFolderRepository,
) {
    suspend operator fun invoke(folder: NoteFolder) {
        requireNotBlank(folder.id, "Folder id")
        val normalizedName = folder.name.trim()
        requireNotBlank(normalizedName, "Folder name")
        val folder = folder.copy(name = normalizedName, updatedAt = Clock.System.now())
        repo.updateFolder(folder)
    }
}
