package com.itlab.domain

import com.itlab.domain.model.NoteFolder
import kotlinx.datetime.Clock
import org.junit.Assert.assertEquals
import org.junit.Test

class NoteFolderTest {
    @Test
    fun folder_creation() {
        val folder = NoteFolder(name = "Test")

        assertEquals("Test", folder.name)
    }

    @Test
    fun folder_copy() {
        val folder =
            NoteFolder(
                name = "Old",
                createdAt = Clock.System.now(),
                updatedAt = Clock.System.now(),
            )

        val updated = folder.copy(name = "New")

        assertEquals("Old", folder.name)
        assertEquals("New", updated.name)
    }
}
