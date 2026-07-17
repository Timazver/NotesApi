package kz.notes.notesapi.users.service

import kz.notes.notesapi.auth.domain.AuthCredentialsEntity
import kz.notes.notesapi.infrastructure.AuthRepository
import kz.notes.notesapi.infrastructure.UserRepository
import kz.notes.notesapi.users.domain.UserEntity
import kz.notes.notesapi.users.domain.exceptions.UserNotFoundException
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
        testUser = UserEntity(id = 1L, firstName = "John", lastName = "Doe", isActive = true)
        testAuthCredentials =
            AuthCredentialsEntity(
                id = 1L,
                email = "john.doe@example.com",
                user = testUser,
                passHash = "hashed_password",
                createdAt = Instant.now(),
            )
    }

    @Test
    fun `getUserInfo should return user when email exists`() {
        // Arrange
        val email = "john.doe@example.com"
        `when`(authRepository.findByEmail(email)).thenReturn(testAuthCredentials)

        // Act
        val result = userService.getUserInfo(email)

        // Assert
        assertEquals("John", result.firstName)
        assertEquals("Doe", result.lastName)
        verify(authRepository).findByEmail(email)
    }

    @Test
    fun `getUserInfo should throw UserNotFoundException when email does not exist`() {
        // Arrange
        val email = "nonexistent@example.com"
        `when`(authRepository.findByEmail(email)).thenReturn(null)

        // Act & Assert
        assertThrows(UserNotFoundException::class.java) {
            userService.getUserInfo(email)
        }
        verify(authRepository).findByEmail(email)
    }

    @Test
    fun `updateUser should update and save user when email exists`() {
        // Arrange
        val email = "john.doe@example.com"
        `when`(authRepository.findByEmail(email)).thenReturn(testAuthCredentials)

        // Act
        userService.updateUser(email, "Jane", "Smith")

        // Assert
        assertEquals("Jane", testUser.firstName)
        assertEquals("Smith", testUser.lastName)
        verify(userRepository).save(testUser)
    }
}
