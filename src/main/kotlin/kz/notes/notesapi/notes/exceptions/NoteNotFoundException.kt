package kz.notes.notesapi.notes.exceptions

class NoteNotFoundException(message: String = "Записи не найдена!") : Exception(message) {
}