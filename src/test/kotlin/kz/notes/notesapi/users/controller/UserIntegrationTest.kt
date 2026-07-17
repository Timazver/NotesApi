package kz.notes.notesapi.users.controller

import kz.notes.notesapi.users.domain.UserEntity
import kz.notes.notesapi.infrastructure.AuthRepository
import kz.notes.notesapi.infrastructure.UserRepository
import kz.notes.notesapi.auth.domain.AuthCredentialsEntity
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.Instant

@SpringBootTest
@AutoConfigureMockMvc
class UserIntegrationTest {

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

        val user = UserEntity(firstName = "Integration", lastName = "Test", isActive = true)
        val savedUser = userRepository.save(user)

        val auth = AuthCredentialsEntity(
            id = null,
            email = "integration@test.com",
            user = savedUser,
            passHash = "hash",
            createdAt = Instant.now()
        )
        authRepository.save(auth)
    }

    @Test
    @WithMockUser(username = "integration@test.com")
    fun `should return current user profile`() {
        mockMvc.perform(get("/users/me"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.firstName").value("Integration"))
            .andExpect(jsonPath("$.data.lastName").value("Test"))
    }

    @Test
    @WithMockUser(username = "integration@test.com")
    fun `should update current user profile`() {
        val updatePayload = """
            {
                "firstName": "Updated",
                "lastName": "Name"
            }
        """.trimIndent()

        mockMvc.perform(
            put("/users/me")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatePayload)
        )
            .andExpect(status().isOk)

        // Verify update
        mockMvc.perform(get("/users/me"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data.firstName").value("Updated"))
            .andExpect(jsonPath("$.data.lastName").value("Name"))
    }

    @Test
    fun `should return 401 when accessing profile without authentication`() {
        mockMvc.perform(get("/users/me"))
            .andExpect(status().isUnauthorized)
    }
}
