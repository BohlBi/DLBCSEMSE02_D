package dlbcsemse02_d.project.domain.model

data class ModeratorRating(
    val id: String,
    val moderatorId: String,
    val score: Int,
    val timestamp: Long,
    val seen: Boolean = false
)
