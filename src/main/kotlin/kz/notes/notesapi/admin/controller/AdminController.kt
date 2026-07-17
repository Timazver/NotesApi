package kz.notes.notesapi.admin.controller

import kz.notes.notesapi.admin.dto.UpdateUserRequestDto
import kz.notes.notesapi.admin.service.AdminService
import kz.notes.notesapi.common.BaseResponse
import kz.notes.notesapi.common.PageResponse
import kz.notes.notesapi.common.toPageResponse
import kz.notes.notesapi.notes.dto.NoteResponseDto
import kz.notes.notesapi.notes.dto.toResponseDto
import kz.notes.notesapi.users.dto.UserResponseDto
import kz.notes.notesapi.users.dto.toResponseDto
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
class AdminController(
    val adminService: AdminService,
) {
    @GetMapping("/users")
    fun getAllUsers(pageable: Pageable): PageResponse<UserResponseDto> {
        val users = adminService.getAllUsers(pageable)
        return users.map { it.toResponseDto() }.toPageResponse()
    }

    @GetMapping("/users/{id}")
    fun getUser(
        @PathVariable id: Long,
    ): BaseResponse<UserResponseDto> {
        val user = adminService.getUser(id).toResponseDto()
        return BaseResponse.success(user)
    }

    @PatchMapping("/users/{id}/role")
    fun changeRole(
        @PathVariable id: Long,
        @RequestBody payload: UpdateUserRequestDto,
    ): BaseResponse<Nothing> {
        adminService.changeRole(id, payload.role)
        return BaseResponse.success(null)
    }

    @PatchMapping("/users/{id}/activate")
    fun activateUser(
        @PathVariable id: Long,
    ): BaseResponse<Nothing> {
        adminService.activateUser(id)
        return BaseResponse.success(null)
    }

    @PatchMapping("/users/{id}/deactivate")
    fun deactivateUser(
        @PathVariable id: Long,
    ): BaseResponse<Nothing> {
        adminService.deactivateUser(id)
        return BaseResponse.success(null)
    }

    @GetMapping("/notes")
    fun getAllNotes(pageable: Pageable): PageResponse<NoteResponseDto> {
        val notes = adminService.getAllNotes(pageable)
        return notes.map { it.toResponseDto() }.toPageResponse()
    }

    @GetMapping("/notes/{id}")
    fun getAllNotes(
        @PathVariable id: Long,
    ): BaseResponse<NoteResponseDto> {
        val notes = adminService.getNote(id)
        return BaseResponse.success(notes.toResponseDto())
    }
}
