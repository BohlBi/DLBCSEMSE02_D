package dlbcsemse02_d.project.infrastructure.repository

import dlbcsemse02_d.project.domain.model.ModeratorRating
import dlbcsemse02_d.project.domain.model.SongRequest

class MockDataStore {
    val moderatorRatings = mutableListOf<ModeratorRating>()
    val songRequests = mutableListOf<SongRequest>()

    private var ratingIdCounter = 0
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
        return request
    }

    fun markRatingsAsSeen(ids: List<String>) {
        ids.forEach { id ->
            val index = moderatorRatings.indexOfFirst { it.id == id }
            if (index >= 0) {
                moderatorRatings[index] = moderatorRatings[index].copy(seen = true)
            }
        }
    }

    fun markSongRequestsAsSeen(ids: List<String>) {
        ids.forEach { id ->
            val index = songRequests.indexOfFirst { it.id == id }
            if (index >= 0) {
                songRequests[index] = songRequests[index].copy(seen = true)
            }
        }
    }

    fun getUnseenCount(): Int {
        return moderatorRatings.count { !it.seen } + songRequests.count { !it.seen }
    }

    private fun nextTimestamp(): Long {
        timestampCounter += 60000L
        return timestampCounter
    }
}
