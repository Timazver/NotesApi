package kz.notes.notesapi.admin.bootstrap

import kz.notes.notesapi.auth.repository.AuthRepository
import kz.notes.notesapi.users.domain.Role
import kz.notes.notesapi.users.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.security.crypto.password.PasswordEncoder
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.postgresql.PostgreSQLContainer

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE,
    args = [
        "--first-name=Bootstrap",
        "--last-name=Admin",
        "--email=bootstrap@example.com",
    ],
    properties = [
        "spring.profiles.active=bootstrap-admin",
        "ADMIN_BOOTSTRAP_PASSWORD=bootstrap-password",
        "jwt.secret=integration-tests-secret-key-at-least-32-characters",
        "jwt.expiration=300",
    ],
)
@Testcontainers
class AdminBootstrapIntegrationTest {
    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val postgres = PostgreSQLContainer("postgres:16-alpine")
    }

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var authRepository: AuthRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun `should bootstrap administrator from application arguments`() {
        val administrator = userRepository.findByEmail("bootstrap@example.com")

        assertNotNull(administrator)
        assertEquals(Role.ADMIN, administrator?.role)

        val credentials = administrator?.let(authRepository::findByUser)
        assertNotNull(credentials)
        assertTrue(passwordEncoder.matches("bootstrap-password", credentials?.passHash))
    }
}
