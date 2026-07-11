package kz.notes.notesapi.auth.domain.exceptions

import org.springframework.security.core.AuthenticationException

class UnauthorizedException(
    message: String = "Требуется авторизация"
) : AuthenticationException(message)
