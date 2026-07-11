package kz.notes.notesapi.users.dto

import jakarta.validation.constraints.NotBlank

data class UpdateUserDto(
    @field:NotBlank
    val firstName: String,
    @field:NotBlank
    val lastName: String
)
