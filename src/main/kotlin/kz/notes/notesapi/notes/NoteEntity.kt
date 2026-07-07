package kz.notes.notesapi.notes

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "notes")
class NoteEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "title")
    var title: String,

    @Column(name="content")
    var content: String,

    @Column(name = "created_at")
    var createdAt: Instant,

    @Column(name = "updated_at")
    var updatedAt: Instant,

    @Column(name = "is_archived")
    var isArchived: Boolean = false,

)