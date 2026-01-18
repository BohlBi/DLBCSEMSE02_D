package dlbcsemse02_d.project.infrastructure.repository

import dlbcsemse02_d.project.domain.model.Song
import dlbcsemse02_d.project.domain.repository.SongRepository
import kotlinx.coroutines.delay

class MockSongRepository : SongRepository {

    private val mockSongs = listOf(
        Song(
            id = "1",
            title = "Bohemian Rhapsody",
            interpret = "Queen",
            album = "A Night at the Opera",
            duration = 354.0f
        ),
        Song(
            id = "2",
            title = "Stairway to Heaven",
            interpret = "Led Zeppelin",
            album = "Led Zeppelin IV",
            duration = 482.0f
        ),
        Song(
            id = "3",
            title = "Hotel California",
            interpret = "Eagles",
            album = "Hotel California",
            duration = 391.0f
        ),
        Song(
            id = "4",
            title = "Imagine",
            interpret = "John Lennon",
            album = "Imagine",
            duration = 187.0f
        )
    )

    private var currentIndex = 0
    private var playlistRating: Int? = null

    override suspend fun getCurrentSong(): Result<Song> {
        delay(500)
        val song = mockSongs[currentIndex]
        return Result.success(song)
    }

    override suspend fun getPlaylist(): Result<List<Song>> {
        delay(300)
        return Result.success(mockSongs)
    }

    override suspend fun ratePlaylist(stars: Int): Result<Unit> {
        delay(200)
        playlistRating = stars
        return Result.success(Unit)
    }

    override suspend fun getPlaylistRating(): Result<Int?> {
        delay(100)
        return Result.success(playlistRating)
    }
}
