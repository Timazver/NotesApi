package kz.notes.notesapi.auth.service

import kz.notes.notesapi.auth.domain.AuthCredentialsEntity
import kz.notes.notesapi.auth.domain.exceptions.EmailAlreadyExistsException
import kz.notes.notesapi.auth.domain.exceptions.WrongCredentialsException
import kz.notes.notesapi.auth.jwt.JwtTokenService
import kz.notes.notesapi.auth.repository.AuthRepository
import kz.notes.notesapi.users.domain.Role
import kz.notes.notesapi.users.domain.UserEntity
import kz.notes.notesapi.users.domain.exceptions.UserNotFoundException
import kz.notes.notesapi.users.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class AuthService(
    private val repo: AuthRepository,
    private val userRepo: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenUtil: JwtTokenService,
) {
    @Transactional
    fun registerUser(command: RegisterUserCommand) {
f        val existed = userRepo.existsByEmail(command.email)
        if (existed) throw EmailAlreadyExistsException("Email already exists")
        val user =
            UserEntity(
                firstName = command.firstName,
                lastName = command.lastName,
                isActive = true,
                email = command.email,
                role = Role.USER,
            )
        val saved = userRepo.save(user)
        val passwordHash = passwordEncoder.encode(command.password)
        val authCredentials =
            AuthCredentialsEntity(
                id = null,
                user = saved,
                passHash = passwordHash!!,
                createdAt = Instant.now(),
            )
        repo.save(authCredentials)
    }

    fun login(
        email: String,
        password: String,
    ): String {
        val existedUser = userRepo.findByEmail(email) ?: throw UserNotFoundException("Email does not exist")
        val credentials = repo.findByUser(existedUser) ?: throw WrongCredentialsException("User not found")
        if (passwordEncoder.matches(password, credentials.passHash)) {
            return jwtTokenUtil.generateToken(email, existedUser.role)
        } else {
            throw WrongCredentialsException("Wrong password")
        }
    }
}
