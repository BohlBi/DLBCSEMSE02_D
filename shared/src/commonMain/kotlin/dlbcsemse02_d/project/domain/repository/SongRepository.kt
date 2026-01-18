package dlbcsemse02_d.project.domain.repository

import dlbcsemse02_d.project.domain.model.Moderator
import dlbcsemse02_d.project.domain.model.ModeratorRating
import dlbcsemse02_d.project.domain.model.Song

interface SongRepository {
    suspend fun getCurrentSong(): Result<Song>
    suspend fun getPlaylist(): Result<List<Song>>
    suspend fun ratePlaylist(stars: Int): Result<Unit>
    suspend fun getPlaylistRating(): Result<Int?>
    suspend fun getCurrentModerator(): Result<Moderator?>
    suspend fun rateModerator(rating: ModeratorRating): Result<Unit>
}
