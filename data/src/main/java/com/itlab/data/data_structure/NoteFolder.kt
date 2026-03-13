package com.itlab.data.data_structure

import kotlinx.datatime.Clock
import kotlinx.datatime.Instant
import java.util.UUID

data class NoteFolder(
    val id : Sting = UUID.randomUUID().toString(),
    val name : String,
    val notes : List<Note> = emptyList(),
    val createdAt : Instant = Clock.System.now(),
    val updatedAt : Instant = Clock.System.now(),
    val metadata : Map<String, String> = emptyMap()
){
    fun addNote(note : Note) : NoteFolder = 
        copy(
            notes = notes + note,
            updatedAt = Clock.System.now()
        )

    fun removeNote(noteId : String) : NoteFolder =
        copy(
            notes = notes.filter { it.id != noteId },
            updatedAt = Clock.System.now()
        )

    fun updateNote(updateNote : Note) : NoteFolder = 
        copy(
            notes = notes.map { if(it.id == updateNote.id) updateNote else it },
            updatedAt = Clock.System.now()
        )

    fun rename(newName : String) : NoteFolder =
        copy(
            name = newName,
            updatedAt = Clock.System.now()
        )

    fun withMetadata(newMetadata : Map<String, String>) : NoteFolder =
        copy(
            metadata = newMetadata,
            updatedAt = Clock.System.now()
        )
}