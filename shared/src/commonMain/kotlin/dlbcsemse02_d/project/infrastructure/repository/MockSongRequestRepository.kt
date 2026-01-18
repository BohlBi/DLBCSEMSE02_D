package dlbcsemse02_d.project.infrastructure.repository

import dlbcsemse02_d.project.domain.model.SongRequest
import dlbcsemse02_d.project.domain.repository.SongRequestRepository
import kotlinx.coroutines.delay

class MockSongRequestRepository : SongRequestRepository {

    private val submittedRequests = mutableListOf<SongRequest>()

    override suspend fun submitSongRequest(request: SongRequest): Result<Unit> {
        delay(500)
        submittedRequests.add(request)
        return Result.success(Unit)
    }
}
