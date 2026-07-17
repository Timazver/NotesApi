package kz.notes.notesapi.common

data class BaseResponse<T>(
    val status: Int,
    val data: T? = null,
    val error: String? = null,
) {
    companion object {
        fun <T> success(
            data: T?,
            status: Int = 200,
        ) = BaseResponse(
            status = status,
            data = data,
        )

        fun error(
            status: Int,
            message: String,
        ) = BaseResponse<Nothing>(
            status = status,
            error = message,
        )
    }
}
