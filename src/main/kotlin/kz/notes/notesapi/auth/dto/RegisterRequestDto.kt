package kz.notes.notesapi.auth.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegisterRequestDto(
    @field:NotBlank(message = EMPTY_FIELD_ERROR)
    @field:Size(max = 30)
    val firstName: String,
    @field:NotBlank(message = EMPTY_FIELD_ERROR)
    @field:Size(max = 30)
    val lastName: String,
    @field:NotBlank(message = EMPTY_FIELD_ERROR)
    @field:Email(message = EMAIL_ERROR)
    @field:Size(max = 255)
    val email: String,
    @field:NotBlank(message = EMPTY_FIELD_ERROR)
    @field:Size(min = 8, max = 72, message = MIN_PASS_LENGTH_ERROR)
    val password: String,
) {
    companion object {
        const val EMPTY_FIELD_ERROR = "Поле не может быть пустым"
        const val EMAIL_ERROR = "Email неверного формата"
        const val MIN_PASS_LENGTH_ERROR = "Минимальная длина пароля 8 символов"
    }
}
