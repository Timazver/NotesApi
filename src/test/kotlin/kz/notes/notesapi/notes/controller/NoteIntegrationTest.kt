package kz.notes.notesapi.notes.controller

import kz.notes.notesapi.auth.domain.AuthCredentialsEntity
import kz.notes.notesapi.auth.repository.AuthRepository
import kz.notes.notesapi.notes.domain.NoteEntity
import kz.notes.notesapi.notes.repository.NoteRepository
import kz.notes.notesapi.users.domain.Role
import kz.notes.notesapi.users.domain.UserEntity
import kz.notes.notesapi.users.repository.UserRepository
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.postgresql.PostgreSQLContainer
import java.time.Instant

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class NoteIntegrationTest {
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

    @Autowired
    private lateinit var noteRepository: NoteRepository

    private var testUserId: Long = 0
    private var firstNoteId: Long = 0

    @BeforeEach
    fun setup() {
        noteRepository.deleteAll()
        authRepository.deleteAll()
        userRepository.deleteAll()

        val user =
            UserEntity(
                firstName = "Test User",
                lastName = "Test User",
                isActive = true,
                email = "test_user@test.com",
                role = Role.USER,
            )
        val savedUser = userRepository.save(user)
        testUserId = savedUser.id!!

        val auth =
            AuthCredentialsEntity(
                id = null,
                user = savedUser,
                passHash = "hash",
                createdAt = Instant.now(),
            )
        authRepository.save(auth)

        val notes =
            listOf<NoteEntity>(
                createNote(user = savedUser),
                createNote(title = "Title 2", user = savedUser),
            )
        firstNoteId = noteRepository.save(notes.first()).id!!
        noteRepository.save(notes.last())
    }

    @Test
    fun `getNotes should return http 401 error`() {
        mockMvc
            .perform(get("/notes"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `getNotes should return all user notes`() {
        mockMvc
            .perform(
                get("/notes")
                    .with(userAuthentication()),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.data.length()").value(2))
            .andExpect(jsonPath("$.data[*].title").value(containsInAnyOrder("Title", "Title 2")))
    }

    @Test
    fun `getNote should return note by id`() {
        mockMvc
            .perform(
                get("/notes/{id}", firstNoteId)
                    .with(userAuthentication()),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value(200))
            .andExpect(jsonPath("$.data.id").value(firstNoteId))
            .andExpect(jsonPath("$.data.title").value("Title"))
            .andExpect(jsonPath("$.data.content").value("Content"))
    }

    @Test
    fun `getNote should return 404 when note does not exist`() {
        mockMvc
            .perform(
                get("/notes/{id}", Long.MAX_VALUE)
                    .with(userAuthentication()),
            ).andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("Записи не найдена!"))
    }

    @Test
    fun `addNote should create note`() {
        val payload =
            """
            {
                "title": "New title",
                "content": "New content"
            }
            """.trimIndent()

        mockMvc
            .perform(
                post("/notes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
                    .with(userAuthentication()),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value(201))

        val createdNote = noteRepository.findAll().single { it.title == "New title" }
        assertEquals("New content", createdNote.content)
        assertTrue(noteRepository.findNoteEntitiesByUserId(testUserId).any { it.id == createdNote.id })
    }

    @Test
    fun `addNote should return 400 when title is blank`() {
        val payload =
            """
            {
                "title": "",
                "content": "New content"
            }
            """.trimIndent()

        mockMvc
            .perform(
                post("/notes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
                    .with(userAuthentication()),
            ).andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.error").value("title: Field cannot be empty"))

        assertEquals(2L, noteRepository.count())
    }

    @Test
    fun `updateNote should update note`() {
        val payload =
            """
            {
                "title": "Updated title",
                "content": "Updated content"
            }
            """.trimIndent()

        mockMvc
            .perform(
                patch("/notes/{id}", firstNoteId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(payload)
                    .with(userAuthentication()),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value(200))

        val updatedNote = noteRepository.findById(firstNoteId).orElseThrow()
        assertEquals("Updated title", updatedNote.title)
        assertEquals("Updated content", updatedNote.content)
    }

    @Test
    fun `deleteNote should delete note`() {
        mockMvc
            .perform(
                delete("/notes/{id}", firstNoteId)
                    .with(userAuthentication()),
            ).andExpect(status().isOk)
            .andExpect(jsonPath("$.status").value(200))

        assertTrue(noteRepository.findById(firstNoteId).isEmpty)
        assertEquals(1L, noteRepository.count())
    }

    @Test
    fun `getNote should return 404 for another user note`() {
        val anotherUser =
            userRepository.save(
                UserEntity(
                    firstName = "Another",
                    lastName = "User",
                    isActive = true,
                    email = "another_user@test.com",
                    role = Role.USER,
                ),
            )
        val anotherUserNoteId = noteRepository.save(createNote(user = anotherUser)).id!!

        mockMvc
            .perform(
                get("/notes/{id}", anotherUserNoteId)
                    .with(userAuthentication()),
            ).andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(404))
    }

    private fun userAuthentication() =
        authentication(
            UsernamePasswordAuthenticationToken(
                "test_user@test.com",
                null,
                emptyList(),
            ),
        )

    private fun createNote(
        title: String = "Title",
        user: UserEntity,
    ): NoteEntity =
        NoteEntity(
            id = null,
            title = title,
            content = "Content",
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            isArchived = false,
            user = user,
        )
}
