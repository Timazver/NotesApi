package kz.notes.notesapi.admin.bootstrap

import kz.notes.notesapi.auth.domain.AuthCredentialsEntity
import kz.notes.notesapi.auth.repository.AuthRepository
import kz.notes.notesapi.users.domain.Role
import kz.notes.notesapi.users.domain.UserEntity
import kz.notes.notesapi.users.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class AdminBootstrapService(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    @Transactional
    fun bootstrap(command: AdminBootstrapCommand): AdminBootstrapResult {
        if (userRepository.existsByRole(Role.ADMIN)) {
            return AdminBootstrapResult.ALREADY_EXISTS
        }

        check(!userRepository.existsByEmail(command.email)) {
            "Cannot bootstrap administrator: email ${command.email} already belongs to another user"
        }

        val user =
            userRepository.save(
                UserEntity(
                    firstName = command.firstName,
                    lastName = command.lastName,
                    isActive = true,
                    email = command.email,
                    role = Role.ADMIN,
                ),
            )
        val credentials =
            AuthCredentialsEntity(
                id = null,
                user = user,
                passHash =
                    requireNotNull(passwordEncoder.encode(command.password)) {
                        "Password encoder returned no hash"
                    },
                createdAt = Instant.now(),
            )

        authRepository.save(credentials)
        return AdminBootstrapResult.CREATED
    }
}
