package kz.notes.notesapi.notes.exceptions

import kz.notes.notesapi.common.NotFoundException

class NoteNotFoundException(message: String = "Записи не найдена!") : NotFoundException(message) {
}