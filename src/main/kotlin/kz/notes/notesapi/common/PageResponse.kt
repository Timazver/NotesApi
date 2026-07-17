package kz.notes.notesapi.common

import org.springframework.data.domain.Page

data class PageResponse<T : Any>(
    val items: List<T>,
    val page: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
)

fun <T : Any> Page<T>.toPageResponse(): PageResponse<T> =
    PageResponse(
        items = content,
        page = number,
        size = size,
        totalElements = totalElements,
        totalPages = totalPages,
    )
