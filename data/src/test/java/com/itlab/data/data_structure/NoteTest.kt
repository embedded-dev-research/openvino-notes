package com.itlab.domain

import com.itlab.domain.model.ContentItem
import com.itlab.domain.model.ImageSource
import com.itlab.domain.model.Note
import com.itlab.domain.model.TextFormat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNotSame
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Date

class NoteTest {
    @Test
    fun createDefaultNote() {
        val note = Note()

        assertNotNull(note.id)
        assertEquals("", note.title)
        assertTrue(note.contentItems.isEmpty())
        assertNotNull(note.createdAt)
        assertNotNull(note.updatedAt)
        assertTrue(note.tags.isEmpty())
        assertFalse(note.isFavorite)
    }

    @Test
    fun createCustomNote() {
        val id = "custom-id"
        val title = "Test Note"
        val content = listOf(ContentItem.Text("Hello"))
        val now = Date()
        val tags = setOf("work", "important")

        val note =
            Note(
                id = id,
                title = title,
                contentItems = content,
                createdAt = now,
                updatedAt = now,
                tags = tags,
                isFavorite = true,
            )

        assertEquals(id, note.id)
        assertEquals(title, note.title)
        assertEquals(content, note.contentItems)
        assertEquals(now, note.createdAt)
        assertEquals(now, note.updatedAt)
        assertEquals(tags, note.tags)
        assertTrue(note.isFavorite)
    }

    @Test
    fun createNoteWithCopy() {
        val note1 = Note(title = "Original")
        val note2 = note1.copy(title = "Copy")

        assertEquals("Original", note1.title)
        assertEquals("Copy", note2.title)
        assertNotSame(note1, note2)
    }

    @Test
    fun addTagViaCopy() {
        val note = Note(tags = setOf("work"))
        val updated = note.copy(tags = note.tags + "personal")

        assertEquals(setOf("work"), note.tags)
        assertEquals(setOf("work", "personal"), updated.tags)
    }

    @Test
    fun removeTagViaCopy() {
        val note = Note(tags = setOf("work", "personal"))
        val updated = note.copy(tags = note.tags - "work")

        assertEquals(setOf("work", "personal"), note.tags)
        assertEquals(setOf("personal"), updated.tags)
    }

    @Test
    fun createTextContent() {
        val text = ContentItem.Text("Hello", TextFormat.MARKDOWN)

        assertEquals("Hello", text.text)
        assertEquals(TextFormat.MARKDOWN, text.format)
    }

    @Test
    fun createLocalImage() {
        val image =
            ContentItem.Image(
                source = ImageSource.Local("/path/image.jpg"),
                width = 800,
                height = 600,
            )

        assertTrue(image.source is ImageSource.Local)
        assertEquals("/path/image.jpg", (image.source as ImageSource.Local).path)
        assertEquals(800, image.width)
        assertEquals(600, image.height)
    }

    @Test
    fun createRemoteImage() {
        val image =
            ContentItem.Image(
                source = ImageSource.Remote("https://example.com/image.jpg"),
            )

        assertTrue(image.source is ImageSource.Remote)
        assertEquals("https://example.com/image.jpg", (image.source as ImageSource.Remote).url)
        assertNull(image.width)
        assertNull(image.height)
    }

    @Test
    fun createFileContent() {
        val file =
            ContentItem.File(
                uri = "/path/doc.pdf",
                mimeType = "application/pdf",
                name = "doc.pdf",
                size = 1024L,
            )

        assertEquals("/path/doc.pdf", file.uri)
        assertEquals("application/pdf", file.mimeType)
        assertEquals("doc.pdf", file.name)
        assertEquals(1024L, file.size)
    }

    @Test
    fun createLinkContent() {
        val link1 = ContentItem.Link("https://kotlinlang.org")
        val link2 =
            ContentItem.Link(
                url = "https://kotlinlang.org",
                title = "Kotlin",
            )

        assertEquals("https://kotlinlang.org", link1.url)
        assertNull(link1.title)

        assertEquals("https://kotlinlang.org", link2.url)
        assertEquals("Kotlin", link2.title)
    }
}
