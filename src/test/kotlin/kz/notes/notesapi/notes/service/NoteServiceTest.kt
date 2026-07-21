package kz.notes.notesapi.notes.service

import kz.notes.notesapi.notes.domain.NoteEntity
import kz.notes.notesapi.notes.domain.exceptions.NoteNotFoundException
import kz.notes.notesapi.notes.repository.NoteRepository
import kz.notes.notesapi.users.domain.Role
import kz.notes.notesapi.users.domain.UserEntity
import kz.notes.notesapi.users.domain.exceptions.UserNotFoundException
import kz.notes.notesapi.users.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertSame

@ExtendWith(MockitoExtension::class)
class NoteServiceTest {
    @Mock
    private lateinit var noteRepository: NoteRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var noteService: NoteService
    private lateinit var testUser: UserEntity
    private lateinit var userNotes: List<NoteEntity>

    @BeforeEach
    fun setup() {
        testUser = createUser()
        userNotes =
            listOf(
                createNote(1L, user = testUser),
                createNote(2L, title = "Title_2", user = testUser),
            )
    }

    @Test
    fun `getNotes should throw UserNotFoundException if no user with provided email`() {
        val email = "test@gmail.com"
        `when`(userRepository.findByEmail(email)).thenReturn(null)
        assertThrows(UserNotFoundException::class.java) { noteService.getNotes(email) }
    }

    @Test
    fun `getNotes should return all notes for User`() {
        `when`(userRepository.findByEmail(testUser.email)).thenReturn(testUser)

        `when`(noteRepository.findNoteEntitiesByUserId(testUser.id!!)).thenReturn(userNotes)
        val result = noteService.getNotes(testUser.email)

        assertEquals(2, result.size)
        assertEquals(1L, result.first().id)
        assertEquals(2L, result.last().id)
    }

    @Test
    fun `getNote should return note belonging to user`() {
        val note = userNotes.first()
        `when`(userRepository.findByEmail(testUser.email)).thenReturn(testUser)
        `when`(noteRepository.getNoteEntityByIdAndUserId(note.id!!, testUser.id!!)).thenReturn(note)

        val result = noteService.getNote(note.id!!, testUser.email)

        assertSame(note, result)
    }

    @Test
    fun `getNote should throw NoteNotFoundException when note does not exist`() {
        val noteId = 100L
        `when`(userRepository.findByEmail(testUser.email)).thenReturn(testUser)
        `when`(noteRepository.getNoteEntityByIdAndUserId(noteId, testUser.id!!)).thenReturn(null)

        assertThrows(NoteNotFoundException::class.java) {
            noteService.getNote(noteId, testUser.email)
        }
    }

    @Test
    fun `addNote should save new note for user`() {
        `when`(userRepository.findByEmail(testUser.email)).thenReturn(testUser)

        noteService.addNote(
            title = "New title",
            content = "New content",
            email = testUser.email,
        )

        val noteCaptor = ArgumentCaptor.forClass(NoteEntity::class.java)
        verify(noteRepository).save(noteCaptor.capture())
        val savedNote = noteCaptor.value
        assertEquals(null, savedNote.id)
        assertEquals("New title", savedNote.title)
        assertEquals("New content", savedNote.content)
        assertSame(testUser, savedNote.user)
        assertFalse(savedNote.isArchived)
        assertEquals(savedNote.createdAt, savedNote.updatedAt)
    }

    @Test
    fun `updateNote should update title and content`() {
        val note = userNotes.first()
        `when`(userRepository.findByEmail(testUser.email)).thenReturn(testUser)
        `when`(noteRepository.getNoteEntityByIdAndUserId(note.id!!, testUser.id!!)).thenReturn(note)

        noteService.updateNote(
            id = note.id!!,
            title = "Updated title",
            content = "Updated content",
            email = testUser.email,
        )

        assertEquals("Updated title", note.title)
        assertEquals("Updated content", note.content)
        verify(noteRepository).save(note)
    }

    @Test
    fun `updateNote should preserve fields when values are null`() {
        val note = userNotes.first()
        val originalTitle = note.title
        val originalContent = note.content
        val originalUpdatedAt = note.updatedAt
        `when`(userRepository.findByEmail(testUser.email)).thenReturn(testUser)
        `when`(noteRepository.getNoteEntityByIdAndUserId(note.id!!, testUser.id!!)).thenReturn(note)

        noteService.updateNote(
            id = note.id!!,
            title = null,
            content = null,
            email = testUser.email,
        )

        assertEquals(originalTitle, note.title)
        assertEquals(originalContent, note.content)
        assertTrue(note.updatedAt >= originalUpdatedAt)
        verify(noteRepository).save(note)
    }

    @Test
    fun `updateNote should throw NoteNotFoundException when note does not exist`() {
        val noteId = 100L
        `when`(userRepository.findByEmail(testUser.email)).thenReturn(testUser)
        `when`(noteRepository.getNoteEntityByIdAndUserId(noteId, testUser.id!!)).thenReturn(null)

        assertThrows(NoteNotFoundException::class.java) {
            noteService.updateNote(
                id = noteId,
                title = "Updated title",
                content = "Updated content",
                email = testUser.email,
            )
        }
    }

    @Test
    fun `deleteNote should delete note belonging to user`() {
        val note = userNotes.first()
        `when`(userRepository.findByEmail(testUser.email)).thenReturn(testUser)
        `when`(noteRepository.getNoteEntityByIdAndUserId(note.id!!, testUser.id!!)).thenReturn(note)

        noteService.deleteNote(note.id!!, testUser.email)

        verify(noteRepository).delete(note)
    }

    @Test
    fun `deleteNote should throw NoteNotFoundException when note does not exist`() {
        val noteId = 100L
        `when`(userRepository.findByEmail(testUser.email)).thenReturn(testUser)
        `when`(noteRepository.getNoteEntityByIdAndUserId(noteId, testUser.id!!)).thenReturn(null)

        assertThrows(NoteNotFoundException::class.java) {
            noteService.deleteNote(noteId, testUser.email)
        }
    }

    private fun createUser(): UserEntity =
        UserEntity(
            id = 1L,
            firstName = "Admin",
            lastName = "Admin",
            isActive = true,
            email = "admin@gmail.com",
            role = Role.ADMIN,
        )

    private fun createNote(
        id: Long,
        title: String = "Title",
        user: UserEntity = testUser,
    ): NoteEntity =
        NoteEntity(
            id = id,
            title = title,
            content = "Content",
            createdAt = Instant.now(),
            updatedAt = Instant.now(),
            isArchived = false,
            user = user,
        )
}
