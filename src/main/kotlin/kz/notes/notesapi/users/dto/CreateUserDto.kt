package kz.notes.notesapi.users.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateUserDto(
    @field:NotBlank("Field cannot be empty")
    @field:Size(max = 30)
    var firstName: String,
    @field:NotBlank("Field cannot be empty")
    var lastName: String,
)
