package kz.notes.notesapi.admin.bootstrap

import kz.notes.notesapi.auth.domain.AuthCredentialsEntity
import kz.notes.notesapi.auth.repository.AuthRepository
import kz.notes.notesapi.users.domain.Role
import kz.notes.notesapi.users.domain.UserEntity
import kz.notes.notesapi.users.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.any
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoInteractions
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder

@ExtendWith(MockitoExtension::class)
class AdminBootstrapServiceTest {
    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var authRepository: AuthRepository

    @Mock
    private lateinit var passwordEncoder: PasswordEncoder

    @InjectMocks
    private lateinit var bootstrapService: AdminBootstrapService

    private val command =
        AdminBootstrapCommand(
            firstName = "Admin",
            lastName = "User",
            email = "admin@example.com",
            password = "secret-password",
        )

    @Test
    fun `should create administrator when no administrator exists`() {
        `when`(userRepository.existsByRole(Role.ADMIN)).thenReturn(false)
        `when`(userRepository.existsByEmail(command.email)).thenReturn(false)
        `when`(userRepository.save(any(UserEntity::class.java))).thenAnswer { it.arguments.first() }
        `when`(passwordEncoder.encode(command.password)).thenReturn("password-hash")

        val result = bootstrapService.bootstrap(command)

        val userCaptor = ArgumentCaptor.forClass(UserEntity::class.java)
        val credentialsCaptor = ArgumentCaptor.forClass(AuthCredentialsEntity::class.java)
        verify(userRepository).save(userCaptor.capture())
        verify(authRepository).save(credentialsCaptor.capture())

        assertEquals(AdminBootstrapResult.CREATED, result)
        assertEquals(Role.ADMIN, userCaptor.value.role)
        assertEquals(command.email, userCaptor.value.email)
        assertEquals("password-hash", credentialsCaptor.value.passHash)
    }

    @Test
    fun `should make no changes when administrator already exists`() {
        `when`(userRepository.existsByRole(Role.ADMIN)).thenReturn(true)

        val result = bootstrapService.bootstrap(command)

        assertEquals(AdminBootstrapResult.ALREADY_EXISTS, result)
        verify(userRepository, never()).save(any(UserEntity::class.java))
        verifyNoInteractions(authRepository, passwordEncoder)
    }

    @Test
    fun `should fail when email belongs to another user`() {
        `when`(userRepository.existsByRole(Role.ADMIN)).thenReturn(false)
        `when`(userRepository.existsByEmail(command.email)).thenReturn(true)

        assertThrows(IllegalStateException::class.java) {
            bootstrapService.bootstrap(command)
        }

        verify(userRepository, never()).save(any(UserEntity::class.java))
        verifyNoInteractions(authRepository, passwordEncoder)
    }
}
