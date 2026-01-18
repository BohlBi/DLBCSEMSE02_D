package dlbcsemse02_d.project.presentation.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dlbcsemse02_d.project.application.service.SongService
import dlbcsemse02_d.project.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class PlaylistIntent {
    data object LoadPlaylist : PlaylistIntent()
    data class RatePlaylist(val stars: Int) : PlaylistIntent()
    data object DismissRatingResult : PlaylistIntent()
}

sealed interface PlaylistMode {
    data object Loading : PlaylistMode
    data object Loaded : PlaylistMode
    data class Error(val message: String) : PlaylistMode
}

sealed interface RatingResult {
    data class Success(val stars: Int) : RatingResult
    data object Error : RatingResult
}

data class PlaylistState(
    val mode: PlaylistMode = PlaylistMode.Loading,
    val songs: List<Song> = emptyList(),
    val rating: Int? = null,
    val ratingResult: RatingResult? = null
)

class PlaylistViewModel(
    private val songService: SongService
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaylistState())
    val uiState: StateFlow<PlaylistState> = _uiState.asStateFlow()

    init {
        loadPlaylist()
    }

    fun onIntent(intent: PlaylistIntent) {
        when (intent) {
            PlaylistIntent.LoadPlaylist -> loadPlaylist()
            is PlaylistIntent.RatePlaylist -> ratePlaylist(intent.stars)
            PlaylistIntent.DismissRatingResult -> dismissRatingResult()
        }
    }

    private fun loadPlaylist() {
        _uiState.update { it.copy(mode = PlaylistMode.Loading) }
        viewModelScope.launch {
            songService.getPlaylist().fold(
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(mode = PlaylistMode.Error(exception.message ?: "Unknown error"))
                    }
                },
                onSuccess = { songs ->
                    val rating = songService.getPlaylistRating().getOrNull()
                    _uiState.update {
                        it.copy(mode = PlaylistMode.Loaded, songs = songs, rating = rating)
                    }
                }
            )
        }
    }

    private fun ratePlaylist(stars: Int) {
        viewModelScope.launch {
            songService.ratePlaylist(stars).fold(
                onFailure = {
                    _uiState.update {
                        it.copy(ratingResult = RatingResult.Error)
                    }
                },
                onSuccess = {
                    _uiState.update {
                        it.copy(rating = stars, ratingResult = RatingResult.Success(stars))
                    }
                }
            )
        }
    }

    private fun dismissRatingResult() {
        _uiState.update {
            it.copy(ratingResult = null)
        }
    }
}
