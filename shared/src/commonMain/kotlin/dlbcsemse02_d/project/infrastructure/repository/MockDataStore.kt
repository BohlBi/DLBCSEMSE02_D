package dlbcsemse02_d.project.infrastructure.repository

import dlbcsemse02_d.project.domain.model.ModeratorRating
import dlbcsemse02_d.project.domain.model.PlaylistRating
import dlbcsemse02_d.project.domain.model.SongRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockDataStore {
    val moderatorRatings = mutableListOf<ModeratorRating>()
    val playlistRatings = mutableListOf<PlaylistRating>()
    val songRequests = mutableListOf<SongRequest>()

    private val _unseenCountFlow = MutableStateFlow(0)
    val unseenCountFlow: StateFlow<Int> = _unseenCountFlow.asStateFlow()

    private var ratingIdCounter = 0
    private var playlistRatingIdCounter = 0
    private var requestIdCounter = 0
    private var timestampCounter = 1700000000000L

    init {
        // Testdaten
        moderatorRatings.add(
            ModeratorRating(
                id = "rating_${++ratingIdCounter}",
                moderatorId = "mod1",
                score = 5,
                timestamp = nextTimestamp(),
                seen = false
            )
        )
        moderatorRatings.add(
            ModeratorRating(
                id = "rating_${++ratingIdCounter}",
                moderatorId = "mod1",
                score = 4,
                timestamp = nextTimestamp(),
                seen = false
            )
        )
        songRequests.add(
            SongRequest(
                id = "request_${++requestIdCounter}",
                artist = "Queen",
                title = "We Will Rock You",
                message = "Bitte spielen!",
                timestamp = nextTimestamp()
            )
        )
        playlistRatings.add(
            PlaylistRating(
                id = "playlist_rating_${++playlistRatingIdCounter}",
                score = 5,
                timestamp = nextTimestamp(),
                seen = false
            )
        )
        playlistRatings.add(
            PlaylistRating(
                id = "playlist_rating_${++playlistRatingIdCounter}",
                score = 3,
                timestamp = nextTimestamp(),
                seen = false
            )
        )
        updateUnseenCount()
    }

    fun addModeratorRating(moderatorId: String, score: Int): ModeratorRating {
        val rating = ModeratorRating(
            id = "rating_${++ratingIdCounter}",
            moderatorId = moderatorId,
            score = score,
            timestamp = nextTimestamp(),
            seen = false
        )
        moderatorRatings.add(rating)
        updateUnseenCount()
        return rating
    }

    fun addSongRequest(artist: String, title: String, message: String?): SongRequest {
        val request = SongRequest(
            id = "request_${++requestIdCounter}",
            artist = artist,
            title = title,
            message = message,
            timestamp = nextTimestamp()
        )
        songRequests.add(request)
        updateUnseenCount()
        return request
    }

    fun addPlaylistRating(score: Int): PlaylistRating {
        val rating = PlaylistRating(
            id = "playlist_rating_${++playlistRatingIdCounter}",
            score = score,
            timestamp = nextTimestamp(),
            seen = false
        )
        playlistRatings.add(rating)
        updateUnseenCount()
        return rating
    }

    fun markRatingsAsSeen(ids: List<String>) {
        ids.forEach { id ->
            val index = moderatorRatings.indexOfFirst { it.id == id }
            if (index >= 0) {
                moderatorRatings[index] = moderatorRatings[index].copy(seen = true)
            }
        }
        updateUnseenCount()
    }

    fun markSongRequestsAsSeen(ids: List<String>) {
        ids.forEach { id ->
            val index = songRequests.indexOfFirst { it.id == id }
            if (index >= 0) {
                songRequests[index] = songRequests[index].copy(seen = true)
            }
        }
        updateUnseenCount()
    }

    fun markPlaylistRatingsAsSeen(ids: List<String>) {
        ids.forEach { id ->
            val index = playlistRatings.indexOfFirst { it.id == id }
            if (index >= 0) {
                playlistRatings[index] = playlistRatings[index].copy(seen = true)
            }
        }
        updateUnseenCount()
    }

    private fun updateUnseenCount() {
        _unseenCountFlow.value = moderatorRatings.count { !it.seen } +
            playlistRatings.count { !it.seen } +
            songRequests.count { !it.seen }
    }

    private fun nextTimestamp(): Long {
        timestampCounter += 60000L
        return timestampCounter
    }
}
