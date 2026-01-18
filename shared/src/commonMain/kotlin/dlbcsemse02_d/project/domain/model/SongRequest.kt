package dlbcsemse02_d.project.domain.model

data class SongRequest(
    val artist: String,
    val title: String,
    val message: String? = null
)
