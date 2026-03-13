package com.itlab.data.structure

import com.itlab.data.structure.NoteFolder
import kotlinx.datetime.Clock
import org.junit.Assert.*
import org.junit.Test
import com.itlab.data.structure.ContentItem
import com.itlab.data.structure.ImageSource
import com.itlab.data.structure.Note
import com.itlab.data.structure.TextFormat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

class NoteFolderTest {
    private val now = Clock.System.now()
    private val sampleNote = Note(title = "Sample", contentItems = emptyList())
    private val anotherNote = Note(title = "Another", contentItems = emptyList())

    @Test
    fun `create folder with default values`() {
        val folder = NoteFolder(name = "Work")

        assertNotNull(folder.id)
        assertEquals("Work", folder.name)
        assertTrue(folder.notes.isEmpty())
        assertTrue(folder.metadata.isEmpty())
    }

    @Test
    fun `add note to folder`() {
        val folder = NoteFolder(name = "Personal")
        val updated = folder.addNote(sampleNote)

        assertEquals(1, updated.notes.size)
        assertEquals(sampleNote, updated.notes.first())
        assertTrue(updated.updatedAt > folder.updatedAt)
    }

    @Test
    fun `remove note from folder`() {
        val folder = NoteFolder(name = "Personal", notes = listOf(sampleNote, anotherNote))
        val updated = folder.removeNote(sampleNote.id)

        assertEquals(1, updated.notes.size)
        assertEquals(anotherNote, updated.notes.first())
        assertTrue(updated.updatedAt > folder.updatedAt)
    }

    @Test
    fun `update note in folder`() {
        val originalNote = sampleNote.copy(title = "Old Title")
        val folder = NoteFolder(name = "Personal", notes = listOf(originalNote))

        val updatedNote = originalNote.copy(title = "New title")
        val updatedFolder = folder.updateNote(updatedNote)

        assertEquals("New title", updatedFolder.notes.first().title)
        assertTrue(updatedFolder.updatedAt > folder.updatedAt)
    }

    @Test
    fun `rename folder`() {
        val folder = NoteFolder(name = "Old")
        val renamed = folder.rename("New")

        assertEquals("New", renamed.name)
        assertTrue(renamed.updatedAt > folder.updatedAt)
    }

    @Test
    fun `update metadata`() {
        val folder = NoteFolder(name = "Work")
        val meta = mapOf("color" to "blue", "icon" to "work")
        val updated = folder.withMetadata(meta)

        assertEquals(meta, updated.metadata)
        assertTrue(updated.updatedAt > folder.updatedAt)
    }

    @Test
    fun `folder immutabiliti - original not modified`() {
        val folder = NoteFolder(name = "Test", notes = listOf(sampleNote))
        folder.addNote(anotherNote)

        assertEquals(1, folder.notes.size)
        assertEquals(sampleNote, folder.notes.first())
    }

}

