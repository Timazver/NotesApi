package kz.notes.notesapi.users.domain;

import jakarta.persistence.*

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "first_name", nullable = false)
    var firstName: String,

    @Column(name = "last_name", nullable = false)
    var lastName: String,

    @Column(name = "is_active")
    var isActive: Boolean = true,

    @Column(name = "email", nullable = false)
    var email: String,

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role
)