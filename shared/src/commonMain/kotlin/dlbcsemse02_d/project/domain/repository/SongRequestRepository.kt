package dlbcsemse02_d.project.domain.repository

import dlbcsemse02_d.project.domain.model.SongRequest

interface SongRequestRepository {
    suspend fun submitSongRequest(request: SongRequest): Result<Unit>
}
