package kz.notes.notesapi.auth.jwt

import jakarta.servlet.DispatcherType
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kz.notes.notesapi.auth.domain.exceptions.UnauthorizedException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenService: JwtTokenService,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return request.dispatcherType == DispatcherType.ERROR ||
                request.servletPath.startsWith(AUTH_PATH_PREFIX) ||
                request.servletPath == ERROR_PATH
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = request.getBearerToken()

        if (token != null && SecurityContextHolder.getContext().authentication == null) {
            val authenticated = authenticateToken(token, request)

            if (!authenticated) {
                jwtAuthenticationEntryPoint.commence(
                    request,
                    response,
                    UnauthorizedException("Недействительный или истекший токен")
                )
                return
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun authenticateToken(token: String, request: HttpServletRequest): Boolean {
        if (!jwtTokenService.validateToken(token)) {
            return false
        }

        val email = jwtTokenService.extractEmail(token)
        val role = jwtTokenService.extractRole(token)
        val authorities = listOf(SimpleGrantedAuthority(role.authority))
        val authentication = UsernamePasswordAuthenticationToken(email, null, authorities)
        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication
        return true
    }

    private fun HttpServletRequest.getBearerToken(): String? {
        val header = getHeader(AUTHORIZATION_HEADER) ?: return null

        return if (header.startsWith(BEARER_PREFIX)) {
            header.removePrefix(BEARER_PREFIX).trim()
        } else {
            null
        }
    }

    private companion object {
        const val AUTHORIZATION_HEADER = "Authorization"
        const val BEARER_PREFIX = "Bearer "
        const val AUTH_PATH_PREFIX = "/auth/"
        const val ERROR_PATH = "/error"
    }
}
