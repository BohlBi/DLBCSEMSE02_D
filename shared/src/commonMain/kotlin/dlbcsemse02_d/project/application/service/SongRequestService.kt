package dlbcsemse02_d.project.application.service

import dlbcsemse02_d.project.domain.model.SongRequest
import dlbcsemse02_d.project.domain.repository.SongRequestRepository

class SongRequestService(
    private val songRequestRepository: SongRequestRepository
) {
    companion object {
        const val MAX_MESSAGE_LENGTH = 200
    }

    suspend fun submitSongRequest(request: SongRequest): Result<Unit> {
        return songRequestRepository.submitSongRequest(request)
    }
}
