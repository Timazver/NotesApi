package kz.notes.notesapi.auth.jwt


import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kz.notes.notesapi.auth.domain.exceptions.UnauthorizedException
import kz.notes.notesapi.common.BaseResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import tools.jackson.databind.ObjectMapper
import java.nio.charset.StandardCharsets

@Component
class JwtAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val message = if (authException is UnauthorizedException) {
            authException.message ?: DEFAULT_MESSAGE
        } else {
            DEFAULT_MESSAGE
        }

        response.status = HttpStatus.UNAUTHORIZED.value()
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        objectMapper.writeValue(
            response.writer,
            BaseResponse.error(
                status = HttpStatus.UNAUTHORIZED.value(),
                message = message
            )
        )
    }

    private companion object {
        const val DEFAULT_MESSAGE = "Требуется авторизация"
    }
}
