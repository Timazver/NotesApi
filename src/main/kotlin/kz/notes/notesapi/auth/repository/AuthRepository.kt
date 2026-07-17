package kz.notes.notesapi.auth.repository

import kz.notes.notesapi.auth.domain.AuthCredentialsEntity
import kz.notes.notesapi.users.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AuthRepository : JpaRepository<AuthCredentialsEntity, Long> {
    fun findByUser(user: UserEntity): AuthCredentialsEntity?
}
