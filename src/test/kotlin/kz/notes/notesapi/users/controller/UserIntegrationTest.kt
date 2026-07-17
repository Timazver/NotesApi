package kz.notes.notesapi.users.controller

import kz.notes.notesapi.auth.domain.AuthCredentialsEntity
import kz.notes.notesapi.auth.repository.AuthRepository
import kz.notes.notesapi.users.domain.Role
import kz.notes.notesapi.users.domain.UserEntity
import kz.notes.notesapi.users.repository.UserRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.postgresql.PostgreSQLContainer
import java.time.Instant

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserIntegrationTest {
    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        val postgres = PostgreSQLContainer("postgres:16-alpine")
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var authRepository: AuthRepository

    @BeforeEach
    fun setUp() {
        authRepository.deleteAll()
        userRepository.deleteAll()

        val user =
            UserEntity(
                firstName = "Integration",
                lastName = "Test",
                isActive = true,
                email = "integration@test.com",
                role = Role.USER,
            )
        val savedUser = userRepository.save(user)

        val auth =
            AuthCredentialsEntity(
                id = null,
                user = savedUser,
                passHash = "hash",
                createdAt = Instant.now(),
            )
        authRepository.save(auth)
    }

    @Test
    fun `should return current user profile`() {
        mockMvc
            .perform(get("/users/me").with(userAuthentication()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.firstName").value("Integration"))
            .andExpect(jsonPath("$.data.lastName").value("Test"))
    }

    @Test
    fun `should update current user profile`() {
        val updatePayload =
            """
            {
                "firstName": "Updated",
                "lastName": "Name"
            }
            """.trimIndent()

        mockMvc
            .perform(
                put("/users/me")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(updatePayload)
                    .with(userAuthentication()),
            ).andExpect(status().isOk)

        mockMvc
            .perform(get("/users/me").with(userAuthentication()))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.firstName").value("Updated"))
            .andExpect(jsonPath("$.data.lastName").value("Name"))
    }

    @Test
    fun `should return 401 when accessing profile without authentication`() {
        mockMvc
            .perform(get("/users/me"))
            .andExpect(status().isUnauthorized)
    }

    private fun userAuthentication() =
        authentication(
            UsernamePasswordAuthenticationToken(
                "integration@test.com",
                null,
                emptyList(),
            ),
        )
}
