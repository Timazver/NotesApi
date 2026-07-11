package kz.notes.notesapi.users.dto

import kz.notes.notesapi.users.domain.UserEntity

data class UserResponseDto(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val isActive: Boolean,
)

fun UserEntity.toResponseDto() = UserResponseDto(
    id = id!!,
    firstName = firstName,
    lastName = lastName,
    isActive = isActive,
)