package dlbcsemse02_d.project.domain.model

data class PlaylistRating(
    val id: String,
    val score: Int,
    val timestamp: Long,
    val seen: Boolean = false
)
