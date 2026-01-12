package dlbcsemse02_d.project.application.service

import dlbcsemse02_d.project.domain.model.Song
import dlbcsemse02_d.project.domain.repository.SongRepository

class SongService(
    private val songRepository: SongRepository
) {
    suspend fun getCurrentSong(): Result<Song> {
        return songRepository.getCurrentSong()
    }
}
