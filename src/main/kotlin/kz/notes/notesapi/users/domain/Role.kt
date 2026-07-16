package kz.notes.notesapi.users.domain

enum class Role(val authority: String) {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER")
}
