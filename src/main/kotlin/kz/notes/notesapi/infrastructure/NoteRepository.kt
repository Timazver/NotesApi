package kz.notes.notesapi.infrastructure

import java.time.Instant
import kz.notes.notesapi.notes.NoteEntity
import org.springframework.data.jpa.repository.JpaRepository

interface NoteRepository : JpaRepository<NoteEntity, Long> {
}
