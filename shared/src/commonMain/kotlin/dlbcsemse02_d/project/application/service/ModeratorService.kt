package dlbcsemse02_d.project.application.service

import dlbcsemse02_d.project.domain.model.ModeratorRating
import dlbcsemse02_d.project.domain.model.PlaylistRating
import dlbcsemse02_d.project.domain.model.SongRequest
import dlbcsemse02_d.project.domain.repository.ModeratorRepository
import kotlinx.coroutines.flow.Flow

class ModeratorService(
    private val moderatorRepository: ModeratorRepository
) {
    suspend fun getModeratorRatings(): Result<List<ModeratorRating>> {
        return moderatorRepository.getModeratorRatings()
    }

    suspend fun markRatingsAsSeen(ratingIds: List<String>): Result<Unit> {
        return moderatorRepository.markRatingsAsSeen(ratingIds)
    }

    suspend fun getPlaylistRatings(): Result<List<PlaylistRating>> {
        return moderatorRepository.getPlaylistRatings()
    }

    suspend fun markPlaylistRatingsAsSeen(ratingIds: List<String>): Result<Unit> {
        return moderatorRepository.markPlaylistRatingsAsSeen(ratingIds)
    }

    suspend fun getSongRequests(): Result<List<SongRequest>> {
        return moderatorRepository.getSongRequests()
    }

    suspend fun markSongRequestsAsSeen(requestIds: List<String>): Result<Unit> {
        return moderatorRepository.markSongRequestsAsSeen(requestIds)
    }

    fun getUnseenCountFlow(): Flow<Int> {
        return moderatorRepository.getUnseenCountFlow()
    }
}
