package kz.notes.notesapi.auth.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import kz.notes.notesapi.users.domain.UserEntity
import java.time.Instant

@Entity
@Table(name = "auth_credentials")
class AuthCredentialsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,
    @Column(name = "password_hash", nullable = false)
    val passHash: String,
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant,
)
