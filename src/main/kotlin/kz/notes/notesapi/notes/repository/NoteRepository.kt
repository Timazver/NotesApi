package kz.notes.notesapi.notes.repository

import kz.notes.notesapi.notes.domain.NoteEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface NoteRepository : JpaRepository<NoteEntity, Long> {
    fun findNoteEntitiesByUserId(userId: Long): List<NoteEntity>

    fun getNoteEntityByIdAndUserId(
        noteId: Long,
        userId: Long,
    ): NoteEntity?

    override fun findAll(pageable: Pageable): Page<NoteEntity>
}
