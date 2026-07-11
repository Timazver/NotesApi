package kz.notes.notesapi.users.domain.exceptions

import kz.notes.notesapi.common.NotFoundException

class UserNotFoundException(message: String = "Пользователь не найден!") : NotFoundException(message) {
}