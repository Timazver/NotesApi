package kz.notes.notesapi.notes.controller

import jakarta.validation.Valid
import kz.notes.notesapi.common.BaseResponse
import kz.notes.notesapi.notes.dto.CreateNoteDto
import kz.notes.notesapi.notes.dto.NoteResponseDto
import kz.notes.notesapi.notes.dto.UpdateNoteDto
import kz.notes.notesapi.notes.dto.toResponseDto
import kz.notes.notesapi.notes.service.NoteService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/notes")
class NoteController(val service: NoteService) {
    @GetMapping
    fun getAllNotes(@AuthenticationPrincipal email: String): BaseResponse<List<NoteResponseDto>> {
        return BaseResponse.Companion.success(service.getNotes(email).map { it -> it.toResponseDto() })
    }

    @GetMapping("/{id}")
    fun getNote(@PathVariable id: Long, @AuthenticationPrincipal email: String): BaseResponse<NoteResponseDto> {
        val note = service.getNote(id, email)
        return BaseResponse.success(note.toResponseDto())
    }

    @PostMapping
    fun addNote(
        @Valid @RequestBody payload: CreateNoteDto,
        @AuthenticationPrincipal email: String
    ): BaseResponse<Nothing> {
        service.addNote(payload.title, payload.content, email = email)
        return BaseResponse.success(data = null, status = 201)
    }

    @PatchMapping("/{id}")
    fun updateNote(
        @PathVariable id: Long,
        @RequestBody payload: UpdateNoteDto,
        @AuthenticationPrincipal email: String
    ): BaseResponse<Nothing> {
        service.updateNote(id, payload.title, payload.content, email)
        return BaseResponse.success(data = null)
    }

    @DeleteMapping("/{id}")
    fun deleteNote(@PathVariable id: Long, @AuthenticationPrincipal email: String): BaseResponse<Nothing> {
        service.deleteNote(id, email)
        return BaseResponse.success(data = null)
    }
}
