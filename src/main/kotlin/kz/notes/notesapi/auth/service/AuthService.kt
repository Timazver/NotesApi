package kz.notes.notesapi.auth.service

import kz.notes.notesapi.auth.domain.AuthCredentialsEntity
import kz.notes.notesapi.auth.domain.exceptions.EmailAlreadyExistsException
import kz.notes.notesapi.infrastructure.AuthRepository
import kz.notes.notesapi.infrastructure.UserRepository
import kz.notes.notesapi.users.domain.UserEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class AuthService(
    private val repo: AuthRepository,
    private val userRepo: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun registerUser(command: RegisterUserCommand) {
        val existed = repo.existsByEmail(command.email);
        if (existed) throw EmailAlreadyExistsException("Email already exists")
        val user = UserEntity(firstName = command.firstName, lastName = command.lastName, isActive = true)
        val saved = userRepo.save(user)
        val passwordHash = passwordEncoder.encode(command.password)
        val authCredentials = AuthCredentialsEntity(
            id = null,
            email = command.email,
            user = saved,
            passHash = passwordHash!!,
            createdAt = Instant.now(),
        )
        repo.save(authCredentials)
    }
}
