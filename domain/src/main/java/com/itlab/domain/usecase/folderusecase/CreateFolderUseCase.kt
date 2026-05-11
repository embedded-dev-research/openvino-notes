package com.itlab.domain.usecase.folderusecase

import com.itlab.domain.model.NoteFolder
import com.itlab.domain.repository.NoteFolderRepository
import com.itlab.domain.usecase.requireNotBlank
import java.util.UUID
import kotlin.time.Clock

class CreateFolderUseCase(
    private val repo: NoteFolderRepository,
) {
    suspend operator fun invoke(folder: NoteFolder): String {
        val normalizedName = folder.name.trim()
        requireNotBlank(normalizedName, "Folder name")
        val now = Clock.System.now()
        val folder =
            folder.copy(
                id = UUID.randomUUID().toString(),
                name = normalizedName,
                createdAt = now,
                updatedAt = now,
            )
        return repo.createFolder(folder)
    }
}
