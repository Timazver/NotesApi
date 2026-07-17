package kz.notes.notesapi.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class AuthRequestDto(
    @field:NotBlank(message = MESSAGE)
    @field:Email(message = EMAIL_ERROR)
    val email: String,
    @field:NotBlank(message = MESSAGE)
    val password: String,
) {
    companion object {
        const val MESSAGE = "Поле не может быть пустым"
        const val EMAIL_ERROR = "Email неверного формата"
    }
}
