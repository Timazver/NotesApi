package kz.notes.notesapi.users.service

import kz.notes.notesapi.infrastructure.UserRepository
import kz.notes.notesapi.users.domain.UserEntity
import kz.notes.notesapi.users.domain.exceptions.UserNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(val repo: UserRepository) {

    fun getAllUsers() = repo.findAll()
    fun getUserInfo(id: Long): UserEntity {
        val note = findNoteOrThrow(id)
        return note
    }

    fun addUser(firstName: String, lastName: String) {
        val entity =
            UserEntity(null, firstName, lastName, true)
        repo.save(entity)
    }

    fun updateNote(id: Long, firstName: String, lastName: String) {
        val user = findNoteOrThrow(id)
        user.firstName = firstName
        user.lastName = lastName
        user.isActive = user.isActive
        repo.save(user)

    }

    fun deleteUser(id: Long) {
        val user = findNoteOrThrow(id)
        repo.delete(user)
    }

    private fun findNoteOrThrow(id: Long): UserEntity = repo.findById(id).orElseThrow { UserNotFoundException() }
}