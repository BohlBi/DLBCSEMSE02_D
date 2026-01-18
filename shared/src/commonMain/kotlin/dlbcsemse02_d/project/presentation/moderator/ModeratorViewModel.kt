package dlbcsemse02_d.project.presentation.moderator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dlbcsemse02_d.project.application.service.ModeratorService
import dlbcsemse02_d.project.domain.model.ModeratorRating
import dlbcsemse02_d.project.domain.model.PlaylistRating
import dlbcsemse02_d.project.domain.model.SongRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed interface ModeratorMode {
    data object Loading : ModeratorMode
    data object Loaded : ModeratorMode
    data class Error(val message: String) : ModeratorMode
}

data class ModeratorState(
    val mode: ModeratorMode = ModeratorMode.Loading,
    val ratings: List<ModeratorRating> = emptyList(),
    val playlistRatings: List<PlaylistRating> = emptyList(),
    val songRequests: List<SongRequest> = emptyList()
)

class ModeratorViewModel(
    private val moderatorService: ModeratorService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModeratorState())
    val uiState: StateFlow<ModeratorState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _uiState.update { it.copy(mode = ModeratorMode.Loading) }
        viewModelScope.launch {
            val ratingsResult = moderatorService.getModeratorRatings()
            val playlistRatingsResult = moderatorService.getPlaylistRatings()
            val requestsResult = moderatorService.getSongRequests()

            ratingsResult.fold(
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(mode = ModeratorMode.Error(exception.message ?: "Unknown error"))
                    }
                },
                onSuccess = { ratings ->
                    playlistRatingsResult.fold(
                        onFailure = { exception ->
                            _uiState.update {
                                it.copy(
                                    mode = ModeratorMode.Error(
                                        exception.message ?: "Unknown error"
                                    )
                                )
                            }
                        },
                        onSuccess = { playlistRatings ->
                            requestsResult.fold(
                                onFailure = { exception ->
                                    _uiState.update {
                                        it.copy(
                                            mode = ModeratorMode.Error(
                                                exception.message ?: "Unknown error"
                                            )
                                        )
                                    }
                                },
                                onSuccess = { requests ->
                                    _uiState.update {
                                        it.copy(
                                            mode = ModeratorMode.Loaded,
                                            ratings = ratings,
                                            playlistRatings = playlistRatings,
                                            songRequests = requests
                                        )
                                    }
                                    markNewRatingsAsSeen(ratings)
                                    markNewPlaylistRatingsAsSeen(playlistRatings)
                                    markNewSongRequestsAsSeen(requests)
                                }
                            )
                        }
                    )
                }
            )
        }
    }

    private fun markNewRatingsAsSeen(ratings: List<ModeratorRating>) {
        val unseenIds = ratings.filter { !it.seen }.map { it.id }
        if (unseenIds.isNotEmpty()) {
            viewModelScope.launch {
                moderatorService.markRatingsAsSeen(unseenIds)
            }
        }
    }

    private fun markNewPlaylistRatingsAsSeen(ratings: List<PlaylistRating>) {
        val unseenIds = ratings.filter { !it.seen }.map { it.id }
        if (unseenIds.isNotEmpty()) {
            viewModelScope.launch {
                moderatorService.markPlaylistRatingsAsSeen(unseenIds)
            }
        }
    }

    private fun markNewSongRequestsAsSeen(requests: List<SongRequest>) {
        val unseenIds = requests.filter { !it.seen }.map { it.id }
        if (unseenIds.isNotEmpty()) {
            viewModelScope.launch {
                moderatorService.markSongRequestsAsSeen(unseenIds)
            }
        }
    }

}
