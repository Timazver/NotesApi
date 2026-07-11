package kz.notes.notesapi.auth.dto

import kz.notes.notesapi.auth.service.RegisterUserCommand

fun RegisterRequestDto.toCommand() = RegisterUserCommand(
    firstName = firstName,
    lastName = lastName,
    email = email,
    password = password,
)
