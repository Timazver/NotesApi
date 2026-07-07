package kz.notes.notesapi.common

import kz.notes.notesapi.notes.exceptions.NoteNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(NoteNotFoundException::class)
    fun handleNoteNotFound(
            e: NoteNotFoundException,
            request: WebRequest
    ): ResponseEntity<BaseResponse<Nothing>> {

        return ResponseEntity(
                BaseResponse.error(status = HttpStatus.NOT_FOUND.value(), e.message!!),
                HttpStatus.NOT_FOUND
        )
    }
}
