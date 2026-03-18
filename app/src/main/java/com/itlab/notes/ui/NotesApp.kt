package com.itlab.notes.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.itlab.notes.ui.notes.DirectoriesScreen
import com.itlab.notes.ui.notes.DirectoryItemUi
import com.itlab.notes.ui.notes.NoteItemUi
import com.itlab.notes.ui.notes.NotesListScreen

@Composable
fun NotesApp() {
    var selectedDirectory: DirectoryItemUi? by remember { mutableStateOf(null) }

    val dir = selectedDirectory
    if (dir == null) {
        DirectoriesScreen(
            onDirectoryClick = { selectedDirectory = it }
        )
    } else {
        NotesListScreen(
            directoryName = dir.name,
            notes = notesFallbackForDirectory(dir),
            onBack = { selectedDirectory = null },
            onNoteClick = { }
        )
    }
}

private fun notesFallbackForDirectory(directory: DirectoryItemUi): List<NoteItemUi> =
    when (directory.name) {
        "My Study" -> listOf(
            NoteItemUi("Lecture notes", "Topic: coroutines\n- suspend\n- scope\n- dispatcher"),
            NoteItemUi("Homework", "Due Friday.\nChecklist:\n1) ...\n2) ...")
        )
        "How to Cook" -> listOf(
            NoteItemUi("Cherry pie", "Ingredients:\n- Flour 300g\n- Cherries 200g\n- Sugar 120g"),
            NoteItemUi("Pasta", "Sauce: tomatoes + garlic + basil.\nTime: 20 minutes.")
        )
        else -> listOf(
            NoteItemUi("First note", "Temporary placeholder while notes load from the data layer."),
            NoteItemUi("Second note", "Connect the data layer and pass the list into UI.")
        )
    }

