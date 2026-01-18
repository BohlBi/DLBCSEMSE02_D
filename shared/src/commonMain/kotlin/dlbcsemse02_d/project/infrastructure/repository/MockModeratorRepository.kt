package dlbcsemse02_d.project.infrastructure.repository

import dlbcsemse02_d.project.domain.model.ModeratorRating
import dlbcsemse02_d.project.domain.model.SongRequest
import dlbcsemse02_d.project.domain.repository.ModeratorRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

class MockModeratorRepository(
    private val dataStore: MockDataStore
) : ModeratorRepository {

    override suspend fun getModeratorRatings(): Result<List<ModeratorRating>> {
        delay(300)
        return Result.success(dataStore.moderatorRatings.sortedByDescending { it.timestamp })
    }

    override suspend fun markRatingsAsSeen(ratingIds: List<String>): Result<Unit> {
        delay(100)
        dataStore.markRatingsAsSeen(ratingIds)
        return Result.success(Unit)
    }

    override suspend fun getSongRequests(): Result<List<SongRequest>> {
        delay(300)
        return Result.success(dataStore.songRequests.sortedByDescending { it.timestamp })
    }

    override suspend fun markSongRequestsAsSeen(requestIds: List<String>): Result<Unit> {
        delay(100)
        dataStore.markSongRequestsAsSeen(requestIds)
        return Result.success(Unit)
    }

    override fun getUnseenCountFlow(): Flow<Int> {
        return dataStore.unseenCountFlow
    }
}
