package kz.notes.notesapi.notes.domain;

import jakarta.persistence.*
import kz.notes.notesapi.users.domain.UserEntity
import java.time.Instant

@Entity
@Table(name = "notes")
class NoteEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "title")
    var title: String,

    @Column(name = "content")
    var content: String,

    @Column(name = "created_at")
    var createdAt: Instant,

    @Column(name = "updated_at")
    var updatedAt: Instant,

    @Column(name = "is_archived")
    var isArchived: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: UserEntity
)