package kz.notes.notesapi.auth.domain

import jakarta.persistence.*
import kz.notes.notesapi.users.domain.UserEntity
import java.time.Instant

@Entity
@Table(name = "auth_credentials")
class AuthCredentialsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    @Column(name = "email", nullable = false)
    val email: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserEntity,

    @Column(name = "password_hash", nullable = false)
    val passHash: String,
    @Column(name = "created_at", nullable = false)
    val createdAt: Instant
)
