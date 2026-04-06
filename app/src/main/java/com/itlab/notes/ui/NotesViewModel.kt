package com.itlab.notes.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itlab.domain.usecase.CreateNoteUseCase
import com.itlab.domain.usecase.DeleteFolderUseCase
import com.itlab.domain.usecase.DeleteNoteUseCase
import com.itlab.domain.usecase.ObserveFoldersUseCase
import com.itlab.domain.usecase.ObserveNotesByFolderUseCase
import com.itlab.domain.usecase.UpdateNoteUseCase
import com.itlab.notes.ui.notes.DirectoryItemUi
import com.itlab.notes.ui.notes.NoteItemUi
import kotlinx.coroutines.Job
import com.itlab.domain.model.Note
import com.itlab.domain.model.NoteFolder
import com.itlab.domain.model.ContentItem
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.util.UUID
import com.itlab.domain.usecase.CreateFolderUseCase

class NotesViewModel(
    private val createFolderUseCase: CreateFolderUseCase,
    private val deleteFolderUseCase: DeleteFolderUseCase,
    private val createNoteUseCase: CreateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val observeNotesByFolderUseCase: ObserveNotesByFolderUseCase,
    private val observeFoldersUseCase: ObserveFoldersUseCase
) : ViewModel(), NotesViewModelContract {
    override var uiState: NotesUiState by mutableStateOf(
        NotesUiState(screen = NotesUiScreen.Directories),
    )
        private set
    private var notesJob: Job? = null
    init {
        viewModelScope.launch {
            observeFoldersUseCase().collect { folders ->
                val existingCounts = uiState.directories.associate { it.id to it.noteCount }
                uiState =
                    uiState.copy(
                        directories =
                            folders.map { folder ->
                                folder.toUi(noteCount = existingCounts[folder.id] ?: 0)
                            },
                    )
            }
        }
    }

    override fun onEvent(event: NotesUiEvent) {
        when (event) {
            is NotesUiEvent.OpenDirectory -> openDirectory(event.directory)
            NotesUiEvent.BackToDirectories -> backToDirectories()
            is NotesUiEvent.OpenNote -> openNote(event.note)
            NotesUiEvent.CreateNote -> createNote()
            is NotesUiEvent.CreateDirectory -> createDirectory(event.name)
            is NotesUiEvent.DeleteDirectory -> deleteDirectory(event.directoryId)
            NotesUiEvent.BackToDirectoryNotes -> backToDirectoryNotes()
            is NotesUiEvent.SaveNote -> saveNote(event.note)
            is NotesUiEvent.DeleteNote -> deleteNote(event.noteId)
        }
    }


    private fun openDirectory(directory: DirectoryItemUi) {
        uiState = uiState.copy(
            screen = NotesUiScreen.DirectoryNotes(directory = directory),
            notes = emptyList(),
        )
        notesJob?.cancel()
        val folderId = directory.id.asDomainFolderId()
        notesJob = viewModelScope.launch {
            observeNotesByFolderUseCase(folderId).collect { notes ->
                uiState = uiState.copy(
                    notes = notes.map { it.toUi() },
                    screen = NotesUiScreen.DirectoryNotes(
                        directory = directory.copy(noteCount = notes.size),
                    ),
                )
            }
        }
    }

    private fun backToDirectories() {
        notesJob?.cancel()
        uiState =
            uiState.copy(
                screen = NotesUiScreen.Directories,
                notes = emptyList(),
            )
    }

    private fun openNote(note: NoteItemUi) {
        val dir = (uiState.screen as? NotesUiScreen.DirectoryNotes)?.directory
        if (dir != null) {
            uiState =
                uiState.copy(
                    screen =
                        NotesUiScreen.NoteEditor(
                            directory = dir,
                            note = note,
                        ),
                )
        }
    }

    private fun createNote() {
        val dir = (uiState.screen as? NotesUiScreen.DirectoryNotes)?.directory
        if (dir != null) {
            val newNote =
                NoteItemUi(
                    id = UUID.randomUUID().toString(),
                    title = "",
                    content = "",
                )
            uiState =
                uiState.copy(
                    screen =
                        NotesUiScreen.NoteEditor(
                            directory = dir,
                            note = newNote,
                        ),
                )
        }
    }

    private fun backToDirectoryNotes() {
        val editor = uiState.screen as? NotesUiScreen.NoteEditor
        if (editor != null) {
            uiState =
                uiState.copy(
                    screen = NotesUiScreen.DirectoryNotes(directory = editor.directory),
                )
        }
    }

    private fun saveNote(note: NoteItemUi) {
        val editor = uiState.screen as? NotesUiScreen.NoteEditor ?: return
        val folderId = editor.directory.id.asDomainFolderId()
        viewModelScope.launch {
            val existing = observeNotesByFolderUseCase(folderId)
                .firstOrNull()
                .orEmpty()
                .any { it.id == note.id }
            val domainNote = note.toDomain(folderId = folderId)
            if (existing) {
                updateNoteUseCase(domainNote)
            } else {
                createNoteUseCase(domainNote)
            }
            uiState = uiState.copy(
                screen = NotesUiScreen.DirectoryNotes(directory = editor.directory),
            )
        }
    }

    private fun deleteNote(noteId: String) {
        viewModelScope.launch {
            deleteNoteUseCase(noteId)
        }
    }

    private fun createDirectory(name: String) {
        val normalized = name.trim()
        if (normalized.isBlank()) return
        viewModelScope.launch {
            createFolderUseCase(
                NoteFolder(name = normalized),
            )
        }
    }

    private fun deleteDirectory(directoryId: String) {
        if (directoryId == "all") return
        viewModelScope.launch {
            deleteFolderUseCase(directoryId)
            if ((uiState.screen as? NotesUiScreen.DirectoryNotes)?.directory?.id == directoryId) {
                backToDirectories()
            }
        }
    }

    override fun onCleared() {
        notesJob?.cancel()
        super.onCleared()
    }
}



private fun NoteFolder.toUi(noteCount: Int): DirectoryItemUi =
    DirectoryItemUi(
        id = id,
        name = name,
        noteCount = noteCount,
    )
private fun Note.toUi(): NoteItemUi =
    NoteItemUi(
        id = id,
        title = title,
        content = contentItems
            .filterIsInstance<ContentItem.Text>()
            .joinToString("\n") { it.text },
    )
private fun NoteItemUi.toDomain(folderId: String?): Note =
    Note(
        id = id,
        title = title,
        folderId = folderId,
        contentItems = listOf(ContentItem.Text(content)),
    )

private fun String.asDomainFolderId(): String? = if (this == "all") null else this
