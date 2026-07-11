package kz.notes.notesapi.infrastructure

import kz.notes.notesapi.notes.domain.NoteEntity
import org.springframework.data.jpa.repository.JpaRepository

interface NoteRepository : JpaRepository<NoteEntity, Long> {
    fun findNoteEntitiesByUserId(userId: Long): List<NoteEntity>
    fun getNoteEntityByIdAndUserId(noteId: Long, userId: Long): NoteEntity?
}
