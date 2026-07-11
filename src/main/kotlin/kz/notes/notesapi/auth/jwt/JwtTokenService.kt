package kz.notes.notesapi.auth.jwt

import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.*

@Component
class JwtTokenService(
    private val properties: JwtConfigProperties
) {
    private val signKey = Keys.hmacShaKeyFor(properties.secret.toByteArray())

    fun generateToken(email: String): String {
        val now = Instant.now()
        return Jwts.builder()
            .subject(email)
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(properties.expiration)))
            .signWith(signKey)
            .compact()
    }

    fun extractEmail(token: String): String {
        return Jwts.parser()
            .verifyWith(signKey)
            .build()
            .parseSignedClaims(token)
            .payload
            .subject
    }

    fun validateToken(token: String?): Boolean {
        if (token.isNullOrBlank()) {
            return false
        }

        try {
            Jwts.parser()
                .verifyWith(signKey)
                .build()
                .parseSignedClaims(token)
            return true
        } catch (e: JwtException) {
            return false // Токен недействителен
        } catch (e: IllegalArgumentException) {
            return false
        }
    }
}
