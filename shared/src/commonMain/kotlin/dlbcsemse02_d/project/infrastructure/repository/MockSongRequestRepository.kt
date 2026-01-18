package dlbcsemse02_d.project.infrastructure.repository

import dlbcsemse02_d.project.domain.model.SongRequest
import dlbcsemse02_d.project.domain.repository.SongRequestRepository
import kotlinx.coroutines.delay

class MockSongRequestRepository(
    private val dataStore: MockDataStore
) : SongRequestRepository {

    override suspend fun submitSongRequest(request: SongRequest): Result<Unit> {
        delay(500)
        dataStore.addSongRequest(request.artist, request.title, request.message)
        return Result.success(Unit)
    }
}
