package kz.notes.notesapi.admin.bootstrap

data class AdminBootstrapCommand(
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
)

enum class AdminBootstrapResult {
    CREATED,
    ALREADY_EXISTS,
}
