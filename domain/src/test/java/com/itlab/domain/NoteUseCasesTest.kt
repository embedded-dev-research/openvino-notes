package com.itlab.domain

import com.itlab.domain.model.Note
import com.itlab.domain.model.NoteFolder
import com.itlab.domain.repository.NoteFolderRepository
import com.itlab.domain.repository.NotesRepository
import com.itlab.domain.usecase.CreateFolderUseCase
import com.itlab.domain.usecase.CreateNoteUseCase
import com.itlab.domain.usecase.DeleteNoteUseCase
import com.itlab.domain.usecase.GetNoteUseCase
import com.itlab.domain.usecase.MoveNoteToFolderUseCase
import com.itlab.domain.usecase.ObserveNotesUseCase
import com.itlab.domain.usecase.UpdateNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NoteUseCasesTest {
    private class FakeNotesRepo : NotesRepository {
        private val store = mutableMapOf<String, Note>()
        private val flow = MutableStateFlow<List<Note>>(emptyList())

        override fun observeNotes() = flow

        override fun observeNotesByFolder(folderId: String) = flow

        override suspend fun getNoteById(id: String): Note? = store[id]

        override suspend fun createNote(note: Note): String {
            val id = requireNotNull(note.id)
            store[id] = note
            flow.value = store.values.toList()
            return id
        }

        override suspend fun updateNote(note: Note) {
            val id = requireNotNull(note.id)
            store[id] = note
            flow.value = store.values.toList()
        }

        override suspend fun deleteNote(id: String) {
            store.remove(id)
            flow.value = store.values.toList()
        }
    }

    private class FakeFolderRepo : NoteFolderRepository {
        private val store = mutableMapOf<String, NoteFolder>()

        override fun observeFolders() = MutableStateFlow(emptyList<NoteFolder>())

        override suspend fun createFolder(folder: NoteFolder): String {
            val id = requireNotNull(folder.id)
            store[id] = folder
            return id
        }

        override suspend fun renameFolder(
            id: String,
            name: String,
        ) = Unit

        override suspend fun deleteFolder(id: String) = Unit

        override suspend fun getFolderById(id: String): NoteFolder? = store[id]

        override suspend fun updateFolder(folder: NoteFolder) = Unit
    }

    @Test
    fun create_update_delete_note() =
        runBlocking {
            val repo = FakeNotesRepo()

            val create = CreateNoteUseCase(repo)
            val update = UpdateNoteUseCase(repo)
            val delete = DeleteNoteUseCase(repo)
            val get = GetNoteUseCase(repo)

            val id = create(Note(title = "A"))

            val updated =
                Note(
                    id = id,
                    title = "B",
                )

            update(updated)

            val result = get(id)
            assertEquals("B", result?.title)

            delete(id)

            val result2 = get(id)
            assertNull(result2)
        }

    @Test
    fun moveNoteToFolder_works() =
        runBlocking {
            val notesRepo = FakeNotesRepo()
            val folderRepo = FakeFolderRepo()

            val move = MoveNoteToFolderUseCase(notesRepo)
            val createNote = CreateNoteUseCase(notesRepo)
            val createFolder = CreateFolderUseCase(folderRepo)

            val folderId = createFolder(NoteFolder(name = "Folder"))
            val noteId = createNote(Note(title = "Note"))

            move(folderId, noteId)

            val updated = notesRepo.getNoteById(noteId)

            assertEquals(folderId, updated?.folderId)
        }

    @Test
    fun observeNotes_returnsData() =
        runBlocking {
            val repo = FakeNotesRepo()
            val observe = ObserveNotesUseCase(repo)
            val create = CreateNoteUseCase(repo)

            create(Note(title = "Test"))

            val list = observe().first()

            assertEquals(1, list.size)
        }
}
