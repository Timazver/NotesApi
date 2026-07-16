package kz.notes.notesapi.admin.dto

import kz.notes.notesapi.users.domain.Role


data class UpdateUserRequestDto(
    val role: Role,
)