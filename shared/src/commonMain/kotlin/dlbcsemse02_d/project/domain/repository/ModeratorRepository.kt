package dlbcsemse02_d.project.domain.repository

import dlbcsemse02_d.project.domain.model.ModeratorRating
import dlbcsemse02_d.project.domain.model.SongRequest

interface ModeratorRepository {
    suspend fun getModeratorRatings(): Result<List<ModeratorRating>>
    suspend fun markRatingsAsSeen(ratingIds: List<String>): Result<Unit>
    suspend fun getSongRequests(): Result<List<SongRequest>>
    suspend fun markSongRequestsAsSeen(requestIds: List<String>): Result<Unit>
    suspend fun getUnseenCount(): Result<Int>
}
