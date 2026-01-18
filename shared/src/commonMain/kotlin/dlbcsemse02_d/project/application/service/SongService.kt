package dlbcsemse02_d.project.application.service

import dlbcsemse02_d.project.domain.model.Moderator
import dlbcsemse02_d.project.domain.model.ModeratorRating
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

    suspend fun getCurrentModerator(): Result<Moderator?> {
        return songRepository.getCurrentModerator()
    }

    suspend fun rateModerator(rating: ModeratorRating): Result<Unit> {
        return songRepository.rateModerator(rating)
    }
}
