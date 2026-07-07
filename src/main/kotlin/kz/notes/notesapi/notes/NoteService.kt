package kz.notes.notesapi.notes

import kz.notes.notesapi.infrastructure.NoteRepository
import kz.notes.notesapi.notes.exceptions.NoteNotFoundException
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class NoteService(val repo: NoteRepository ) {

    fun getNotes() = repo.findAll()

    fun getNote(id: Long): NoteEntity {
        val note = findNoteOrThrow(id)
        return note
    }

    fun addNote(title: String, content: String) {
        val entity =
            NoteEntity(null, title, content, createdAt = Instant.now(), Instant.now(), false)
        repo.save(entity)
    }

    fun updateNote(id: Long, title: String?, content: String?) {
        val note = findNoteOrThrow(id)
        note.title = title ?: note.title
        note.content = content ?: note.content
        note.updatedAt = Instant.now()
        repo.save(note)

    }

    fun deleteNote(id: Long){
        val note = findNoteOrThrow(id)
        repo.delete(note)
    }

    private fun findNoteOrThrow(id: Long): NoteEntity =repo.findById(id).orElseThrow {NoteNotFoundException()}
}