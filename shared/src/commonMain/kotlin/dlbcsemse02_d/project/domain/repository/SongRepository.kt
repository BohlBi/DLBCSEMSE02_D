package dlbcsemse02_d.project.domain.repository

import dlbcsemse02_d.project.domain.model.Song

interface SongRepository {
    suspend fun getCurrentSong(): Result<Song>
}
