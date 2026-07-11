package kz.notes.notesapi.infrastructure

import kz.notes.notesapi.users.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long> {
    
}