package com.itlab.notes.ui

import androidx.compose.runtime.Composable
// import org.koin.androidx.compose.koinViewModel

@Composable
fun notesApp() {
//    val viewModel: NotesViewModel = koinViewModel()
//    val state = viewModel.uiState
//
//    when (val screen = state.screen) {
//        NotesUiScreen.Directories -> {
//            directoriesScreen(
//                directories = state.directories,
//                onCreateDirectory = { name ->
//                    viewModel.onEvent(NotesUiEvent.CreateDirectory(name))
//                },
//                onDeleteDirectory = { directory ->
//                    viewModel.onEvent(NotesUiEvent.DeleteDirectory(directory.id))
//                },
//                onDirectoryClick = { directory ->
//                    viewModel.onEvent(NotesUiEvent.OpenDirectory(directory))
//                },
//            )
//        }
//
//        is NotesUiScreen.DirectoryNotes -> {
//            val directory: DirectoryItemUi = screen.directory
//            notesListScreen(
//                directoryName = directory.name,
//                notes = state.notes,
//                actions =
//                    NotesListActions(
//                        onBack = { viewModel.onEvent(NotesUiEvent.BackToDirectories) },
//                        onAddNoteClick = { viewModel.onEvent(NotesUiEvent.CreateNote) },
//                        onNoteDelete = { note -> viewModel.onEvent(NotesUiEvent.DeleteNote(note.id)) },
//                        onNoteClick = { note: NoteItemUi ->
//                            viewModel.onEvent(NotesUiEvent.OpenNote(note))
//                        },
//                    ),
//            )
//        }
//
//        is NotesUiScreen.NoteEditor -> {
//            editorScreen(
//                directoryName = screen.directory.name,
//                note = screen.note,
//                onBack = { viewModel.onEvent(NotesUiEvent.BackToDirectoryNotes) },
//                onSave = { updated -> viewModel.onEvent(NotesUiEvent.SaveNote(updated)) },
//            )
//        }
//    }
}
