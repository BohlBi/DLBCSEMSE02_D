package dlbcsemse02_d.project.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dlbcsemse02_d.project.application.service.SongService
import dlbcsemse02_d.project.domain.exception.SongException
import dlbcsemse02_d.project.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed class NowPlayingIntent {
    data object StartPlaying : NowPlayingIntent()
    data object StopPlaying : NowPlayingIntent()
}

sealed interface CurrentMode {
    data object Loading : CurrentMode
    data object Idle : CurrentMode
    data object Playing : CurrentMode
    data class Error(val message: String) : CurrentMode
}

data class PlayingState(
    val mode: CurrentMode = CurrentMode.Idle,
    val song: Song? = null,
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
        }
    }

    private fun loadCurrentSong() {
        viewModelScope.launch {
            songService.getCurrentSong().fold(
                onFailure = { }, onSuccess = { song ->
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
                }, onSuccess = { song ->
                    _uiState.update {
                        it.copy(mode = CurrentMode.Playing, song = song)
                    }
                }
            )
        }
    }
}