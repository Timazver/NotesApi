package kz.notes.notesapi.users.service

import kz.notes.notesapi.auth.repository.AuthRepository
import kz.notes.notesapi.users.domain.UserEntity
import kz.notes.notesapi.users.domain.exceptions.UserNotFoundException
import kz.notes.notesapi.users.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val repo: UserRepository,
    private val authRepository: AuthRepository,
) {
    fun getUserInfo(email: String): UserEntity = findUserOrThrow(email)

    fun updateUser(
        email: String,
        firstName: String,
        lastName: String,
    ) {
        val user = findUserOrThrow(email)
        user.firstName = firstName
        user.lastName = lastName
        repo.save(user)
    }

    @Transactional
    fun deleteUser(email: String) {
        val user = repo.findByEmail(email) ?: throw UserNotFoundException()
        val authCredentials = authRepository.findByUser(user) ?: throw UserNotFoundException()
        authRepository.delete(authCredentials)
        repo.delete(authCredentials.user)
    }

    private fun findUserOrThrow(email: String): UserEntity = repo.findByEmail(email) ?: throw UserNotFoundException()
}
