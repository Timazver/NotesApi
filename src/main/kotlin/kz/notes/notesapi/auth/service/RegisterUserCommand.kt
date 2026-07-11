package kz.notes.notesapi.auth.service

data class RegisterUserCommand(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)
