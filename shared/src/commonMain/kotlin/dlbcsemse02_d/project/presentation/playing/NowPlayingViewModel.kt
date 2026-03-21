package dlbcsemse02_d.project.presentation.playing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dlbcsemse02_d.project.application.service.SongService
import dlbcsemse02_d.project.domain.exception.SongException
import dlbcsemse02_d.project.domain.model.Moderator
import dlbcsemse02_d.project.domain.model.ModeratorRating
import dlbcsemse02_d.project.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class NowPlayingIntent {
    data object StartPlaying : NowPlayingIntent()
    data object StopPlaying : NowPlayingIntent()
    data object OpenModeratorRating : NowPlayingIntent()
    data object CloseModeratorRating : NowPlayingIntent()
    data class SubmitModeratorRating(val score: Int) : NowPlayingIntent()
    data object DismissRatingResult : NowPlayingIntent()
}

sealed interface CurrentMode {
    data object Loading : CurrentMode
    data object Idle : CurrentMode
    data object Playing : CurrentMode
    data class Error(val message: String) : CurrentMode
}

sealed interface ModeratorRatingResult {
    data object Success : ModeratorRatingResult
    data object Error : ModeratorRatingResult
    data object NoRatingSelected : ModeratorRatingResult
}

data class PlayingState(
    val mode: CurrentMode = CurrentMode.Idle,
    val song: Song? = null,
    val showModeratorSheet: Boolean = false,
    val moderator: Moderator? = null,
    val isLoadingModerator: Boolean = false,
    val isSubmittingRating: Boolean = false,
    val ratingResult: ModeratorRatingResult? = null
)

class NowPlayingViewModel(
    private val songService: SongService
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlayingState())
    val uiState: StateFlow<PlayingState> = _uiState.asStateFlow()

    init {
        loadCurrentSong()
    }

    fun onIntent(intent: NowPlayingIntent) {
        when (intent) {
            NowPlayingIntent.StartPlaying -> {
                _uiState.update { it.copy(mode = CurrentMode.Loading) }
                playCurrentSong()
            }
            NowPlayingIntent.StopPlaying -> {
                _uiState.update { it.copy(mode = CurrentMode.Idle) }
            }
            NowPlayingIntent.OpenModeratorRating -> openModeratorRating()
            NowPlayingIntent.CloseModeratorRating -> closeModeratorRating()
            is NowPlayingIntent.SubmitModeratorRating -> submitModeratorRating(intent.score)
            NowPlayingIntent.DismissRatingResult -> dismissRatingResult()
        }
    }

    private fun loadCurrentSong() {
        viewModelScope.launch {
            songService.getCurrentSong().fold(
                onFailure = { },
                onSuccess = { song ->
                    _uiState.update {
                        it.copy(song = song)
                    }
                }
            )
        }
    }

    private fun playCurrentSong() {
        viewModelScope.launch {
            songService.getCurrentSong().fold(
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            mode = CurrentMode.Error(
                                message = when (exception) {
                                    is SongException.NetworkError -> "Network error. Please try again."
                                    is SongException.NotFound -> "Song not found"
                                    else -> "An unknown error occurred"
                                }
                            )
                        )
                    }
                },
                onSuccess = { song ->
                    _uiState.update {
                        it.copy(mode = CurrentMode.Playing, song = song)
                    }
                }
            )
        }
    }

    private fun openModeratorRating() {
        _uiState.update { it.copy(showModeratorSheet = true, isLoadingModerator = true) }
        viewModelScope.launch {
            songService.getCurrentModerator().fold(
                onFailure = {
                    _uiState.update { it.copy(moderator = null, isLoadingModerator = false) }
                },
                onSuccess = { moderator ->
                    _uiState.update { it.copy(moderator = moderator, isLoadingModerator = false) }
                }
            )
        }
    }

    private fun closeModeratorRating() {
        _uiState.update { it.copy(showModeratorSheet = false, ratingResult = null) }
    }

    private fun submitModeratorRating(score: Int) {
        val moderator = _uiState.value.moderator ?: return

        _uiState.update { it.copy(isSubmittingRating = true) }

        viewModelScope.launch {
            val rating = ModeratorRating(
                id = "",
                moderatorId = moderator.id,
                score = score,
                timestamp = 0L
            )

            songService.rateModerator(rating).fold(
                onFailure = {
                    _uiState.update {
                        it.copy(isSubmittingRating = false, ratingResult = ModeratorRatingResult.Error)
                    }
                },
                onSuccess = {
                    _uiState.update {
                        it.copy(isSubmittingRating = false, ratingResult = ModeratorRatingResult.Success)
                    }
                }
            )
        }
    }

    private fun dismissRatingResult() {
        _uiState.update { it.copy(ratingResult = null) }
    }
}
