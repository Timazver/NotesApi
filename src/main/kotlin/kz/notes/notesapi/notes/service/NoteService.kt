package kz.notes.notesapi.notes.service

import kz.notes.notesapi.infrastructure.AuthRepository
import kz.notes.notesapi.infrastructure.NoteRepository
import kz.notes.notesapi.infrastructure.UserRepository
import kz.notes.notesapi.notes.domain.NoteEntity
import kz.notes.notesapi.notes.exceptions.NoteNotFoundException
import kz.notes.notesapi.users.domain.UserEntity
import kz.notes.notesapi.users.domain.exceptions.UserNotFoundException
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class NoteService(
    private val repo: NoteRepository,
    private val userRepo: UserRepository,
    private val authRepository: AuthRepository
) {

    fun getNotes(email: String): List<NoteEntity> {
        val user = findUserOrThrow(email)
        return repo.findNoteEntitiesByUserId(user.id!!)
    }

    fun getNote(id: Long, email: String): NoteEntity {
        val user = findUserOrThrow(email)
        val note = repo.getNoteEntityByIdAndUserId(id, user.id!!)
        return note ?: throw NoteNotFoundException()
    }

    fun addNote(title: String, content: String, email: String) {
        val now = Instant.now()
        val user = findUserOrThrow(email)
        val entity = NoteEntity(
            id = null,
            title = title,
            content = content,
            createdAt = now,
            updatedAt = now,
            isArchived = false,
            user = user
        )
        repo.save(entity)
    }

    fun updateNote(id: Long, title: String?, content: String?, email: String) {
        val user = findUserOrThrow(email)
        val existed = repo.getNoteEntityByIdAndUserId(id, user.id!!) ?: throw NoteNotFoundException()
        existed.title = title ?: existed.title
        existed.content = content ?: existed.content
        existed.updatedAt = Instant.now()
        repo.save(existed)

    }

    fun deleteNote(id: Long, email: String) {
        val user = findUserOrThrow(email)
        val existed = repo.getNoteEntityByIdAndUserId(id, user.id!!) ?: throw NoteNotFoundException()
        repo.delete(existed)
    }
    
    private fun findUserOrThrow(email: String): UserEntity {
        val authCredentials = authRepository.findByEmail(email) ?: throw UserNotFoundException()
        return authCredentials.user
    }
}