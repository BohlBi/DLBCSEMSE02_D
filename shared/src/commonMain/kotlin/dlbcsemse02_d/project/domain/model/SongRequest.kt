package dlbcsemse02_d.project.domain.model

data class SongRequest(
    val id: String,
    val artist: String,
    val title: String,
    val message: String? = null,
    val timestamp: Long,
    val seen: Boolean = false
)
