package dlbcsemse02_d.project.domain.exception

sealed class SongException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    data class NotFound(val songId: String? = null) :
        SongException("Song not found${songId?.let { ": $it" } ?: ""}")

    data class NetworkError(override val cause: Throwable?) :
        SongException("Network error occurred", cause)

}
