package kz.notes.notesapi.auth.jwt

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.time.Instant
import java.util.Date
import kz.notes.notesapi.users.domain.Role
import org.springframework.stereotype.Component

@Component
class JwtTokenService(
    private val properties: JwtConfigProperties,
) {
    private val signKey = Keys.hmacShaKeyFor(properties.secret.toByteArray())

    fun generateToken(
        email: String,
        role: Role,
    ): String {
        val now = Instant.now()
        return Jwts
            .builder()
            .subject(email)
            .claim(ROLE_CLAIM, role.name)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(properties.expiration)))
            .signWith(signKey)
            .compact()
    }

    fun extractEmail(token: String): String =
        Jwts
            .parser()
            .verifyWith(signKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject

    fun extractRole(token: String): Role {
        val role =
            Jwts
                .parser()
                .verifyWith(signKey)
                .build()
                .parseSignedClaims(token)
                .payload
                .get(ROLE_CLAIM, String::class.java)

        return Role.valueOf(role)
    }

    fun validateToken(token: String?): Boolean {
        if (token.isNullOrBlank()) {
            return false
        }
        try {
            val claims =
                Jwts
                    .parser()
                    .verifyWith(signKey)
                    .build()
                    .parseSignedClaims(token)
                    .payload
            val role = claims.get(ROLE_CLAIM, String::class.java)

            return !claims.subject.isNullOrBlank() && Role.entries.any { it.name == role }
        } catch (_: JwtException) {
            return false // Токен недействителен
        } catch (_: IllegalArgumentException) {
            return false
        }
    }

    private companion object {
        const val ROLE_CLAIM = "role"
    }
}
