package kz.notes.notesapi.users.dto

import kz.notes.notesapi.users.domain.Role
import kz.notes.notesapi.users.domain.UserEntity

data class UserResponseDto(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val isActive: Boolean,
    val email: String,
    val role: Role,
)

fun UserEntity.toResponseDto() =
    UserResponseDto(
        id = id!!,
        firstName = firstName,
        lastName = lastName,
        isActive = isActive,
        email = email,
        role = role,
    )
