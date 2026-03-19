package com.itlab.data.entity

import kotlinx.datetime.Clock
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NoteEntityTest {
    @Test
    fun `when NoteEntity is created with minimum args, default values are set correctly`() {
        val beforeCreation = Clock.System.now()

        val note =
            NoteEntity(
                id = "note_1",
                title = "Test Title",
                content = "Test Content",
            )
        val afterCreation = Clock.System.now()

        assertEquals("note_1", note.id)
        assertEquals("Test Title", note.title)
        assertEquals("Test Content", note.content)
        assertFalse(note.isSynced)
        assertTrue(note.createdAt >= beforeCreation && note.createdAt <= afterCreation)
        assertTrue(note.updatedAt >= beforeCreation && note.updatedAt <= afterCreation)
    }

    @Test fun `when NoteEntity is fully initialized, all fields match`() {
        val customTime = Clock.System.now()

        val note =
            NoteEntity(
                id = "note_2",
                title = "Title 2",
                content = "Content 2",
                createdAt = customTime,
                updatedAt = customTime,
                isSynced = true,
            )

        assertEquals(customTime, note.createdAt)
        assertEquals(customTime, note.updatedAt)
        assertTrue(note.isSynced)
    }

    @Test
    fun `note creation and properties`() {
        val note = NoteEntity(id = "1", title = "Title", content = "Content")
        assertEquals("1", note.id)
        assertEquals("Title", note.title)
        assertEquals(false, note.isSynced)
    }

    @Test
    fun `note equality and hashcode`() {
        val id = "1"
        val title = "A"
        val content = "B"
        val timestamp = kotlinx.datetime.Instant.fromEpochMilliseconds(123456789L)

        val note1 =
            NoteEntity(
                id = id,
                title = title,
                content = content,
                isSynced = false,
                createdAt = timestamp,
                updatedAt = timestamp,
            )
        val note2 =
            NoteEntity(
                id = id,
                title = title,
                content = content,
                isSynced = false,
                createdAt = timestamp,
                updatedAt = timestamp,
            )

        assertEquals(note1, note2)
        assertEquals(note1.hashCode(), note2.hashCode())
    }

    @Test
    fun `note copy updates fields`() {
        val note = NoteEntity("1", "Old", "Text")
        val updated = note.copy(title = "New", isSynced = true)

        assertEquals("New", updated.title)
        assertEquals(true, updated.isSynced)
        assertEquals("1", updated.id)
    }
}
