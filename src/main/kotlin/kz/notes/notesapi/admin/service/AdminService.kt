package kz.notes.notesapi.admin.service

import kz.notes.notesapi.notes.domain.NoteEntity
import kz.notes.notesapi.notes.domain.exceptions.NoteNotFoundException
import kz.notes.notesapi.notes.repository.NoteRepository
import kz.notes.notesapi.users.domain.Role
import kz.notes.notesapi.users.domain.UserEntity
import kz.notes.notesapi.users.domain.exceptions.UserNotFoundException
import kz.notes.notesapi.users.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class AdminService(
    private val repository: UserRepository,
    private val notesRepository: NoteRepository,
) {
    // Users
    fun getAllUsers(pageable: Pageable): Page<UserEntity> = repository.findAll(pageable)

    fun getUser(id: Long): UserEntity = getExistedUserOrThrow(id)

    fun activateUser(id: Long) {
        val user = getExistedUserOrThrow(id)
        if (user.isActive) return
        user.isActive = true
        repository.save(user)
    }

    fun deactivateUser(id: Long) {
        val user = getExistedUserOrThrow(id)
        if (!user.isActive) return
        user.isActive = false
        repository.save(user)
    }

    fun changeRole(
        id: Long,
        role: Role,
    ) {
        val user = getExistedUserOrThrow(id)
        if (user.role == role) return
        user.role = role
        repository.save(user)
    }

    // Notes
    fun getAllNotes(pageable: Pageable): Page<NoteEntity> = notesRepository.findAll(pageable)

    fun getNote(id: Long): NoteEntity {
        val existed = notesRepository.findById(id)
        if (existed.isEmpty) throw NoteNotFoundException()
        return existed.get()
    }

    private fun getExistedUserOrThrow(id: Long): UserEntity {
        val existed = repository.findById(id)
        if (existed.isEmpty) throw UserNotFoundException()
        return existed.get()
    }
}
