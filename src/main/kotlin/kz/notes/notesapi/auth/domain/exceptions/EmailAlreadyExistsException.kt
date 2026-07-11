package kz.notes.notesapi.auth.domain.exceptions

class EmailAlreadyExistsException(message: String = "Пользователь с таким email уже существует") : Exception(message)