package kz.notes.notesapi.notes.dto

import kz.notes.notesapi.notes.domain.NoteEntity
import java.time.Instant

data class NoteResponseDto(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)

fun NoteEntity.toResponseDto() = NoteResponseDto(
    id = id!!,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = updatedAt,
)