package kz.notes.notesapi.common

import kz.notes.notesapi.auth.domain.exceptions.EmailAlreadyExistsException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest

@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun handleNoteNotFound(
        e: NotFoundException,
        request: WebRequest
    ): ResponseEntity<BaseResponse<Nothing>> {

        return ResponseEntity(
            BaseResponse.error(status = HttpStatus.NOT_FOUND.value(), e.message!!),
            HttpStatus.NOT_FOUND
        )
    }


    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<BaseResponse<Nothing>> {
        val message = ex.bindingResult
            .fieldErrors
            .sortedBy { it.field }
            .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }

        return ResponseEntity(
            BaseResponse.error(
                status = HttpStatus.BAD_REQUEST.value(),
                message = message
            ),
            HttpStatus.BAD_REQUEST
        )
    }


    @ExceptionHandler(EmailAlreadyExistsException::class)
    fun handleEmailExistsException(ex: EmailAlreadyExistsException): ResponseEntity<BaseResponse<Nothing>> {
        return ResponseEntity(
            BaseResponse.error(status = HttpStatus.CONFLICT.value(), ex.message!!),
            HttpStatus.CONFLICT
        )
    }
}
