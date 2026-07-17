package kz.notes.notesapi.users.service

import kz.notes.notesapi.auth.domain.AuthCredentialsEntity
import kz.notes.notesapi.auth.repository.AuthRepository
import kz.notes.notesapi.users.domain.Role
import kz.notes.notesapi.users.domain.UserEntity
import kz.notes.notesapi.users.domain.exceptions.UserNotFoundException
import kz.notes.notesapi.users.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant

@ExtendWith(MockitoExtension::class)
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @Mock
    private lateinit var authRepository: AuthRepository

    @InjectMocks
    private lateinit var userService: UserService

    private lateinit var testUser: UserEntity
    private lateinit var testAuthCredentials: AuthCredentialsEntity

    @BeforeEach
    fun setUp() {
        testUser = UserEntity(
            id = 1L,
            firstName = "John",
            lastName = "Doe",
            isActive = true,
            email = "john@example.com",
            role = Role.USER,
        )
        testAuthCredentials = AuthCredentialsEntity(
            id = 1L,
            user = testUser,
            passHash = "hashed_password",
            createdAt = Instant.now(),
        )
    }

    @Test
    fun `getUserInfo should return user when email exists`() {
        val email = "john@example.com"
        `when`(userRepository.findByEmail(email)).thenReturn(testUser)

        val result = userService.getUserInfo(email)

        assertEquals("John", result.firstName)
        assertEquals("Doe", result.lastName)
        verify(userRepository).findByEmail(email)
    }

    @Test
    fun `getUserInfo should throw UserNotFoundException when email does not exist`() {
        val email = "nonexistent@example.com"
        `when`(userRepository.findByEmail(email)).thenReturn(null)

        assertThrows(UserNotFoundException::class.java) {
            userService.getUserInfo(email)
        }
        verify(userRepository).findByEmail(email)
    }

    @Test
    fun `updateUser should update and save user when email exists`() {
        val email = "john@example.com"
        `when`(userRepository.findByEmail(email)).thenReturn(testUser)

        userService.updateUser(email, "Jane", "Smith")

        assertEquals("Jane", testUser.firstName)
        assertEquals("Smith", testUser.lastName)
        verify(userRepository).save(testUser)
    }
}
