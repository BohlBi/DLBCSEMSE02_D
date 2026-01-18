package dlbcsemse02_d.project.application.service

import dlbcsemse02_d.project.domain.model.Song
import dlbcsemse02_d.project.domain.repository.SongRepository

class SongService(
    private val songRepository: SongRepository
) {
    suspend fun getCurrentSong(): Result<Song> {
        return songRepository.getCurrentSong()
    }

    suspend fun getPlaylist(): Result<List<Song>> {
        return songRepository.getPlaylist()
    }

    suspend fun ratePlaylist(stars: Int): Result<Unit> {
        return songRepository.ratePlaylist(stars)
    }

    suspend fun getPlaylistRating(): Result<Int?> {
        return songRepository.getPlaylistRating()
    }
}
