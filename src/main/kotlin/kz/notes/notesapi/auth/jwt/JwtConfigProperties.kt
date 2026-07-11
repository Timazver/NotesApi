package kz.notes.notesapi.auth.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtConfigProperties(
    val secret: String,
    val expiration: Long,
)
