package kz.notes.notesapi.users.repository

import kz.notes.notesapi.users.domain.UserEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun existsByEmail(email: String): Boolean

    fun findByEmail(email: String): UserEntity?

    override fun findAll(pageable: Pageable): Page<UserEntity>
}
