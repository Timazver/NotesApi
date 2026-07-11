package kz.notes.notesapi.infrastructure

import kz.notes.notesapi.auth.domain.AuthCredentialsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AuthRepository : JpaRepository<AuthCredentialsEntity, Long> {
    fun existsByEmail(email: String): Boolean
    fun findByEmail(email: String): AuthCredentialsEntity?
}