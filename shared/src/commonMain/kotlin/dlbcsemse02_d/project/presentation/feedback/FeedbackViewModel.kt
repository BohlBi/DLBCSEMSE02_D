package dlbcsemse02_d.project.presentation.feedback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dlbcsemse02_d.project.application.service.SongRequestService
import dlbcsemse02_d.project.domain.model.SongRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class FeedbackIntent {
    data class SubmitRequest(val artist: String, val title: String, val message: String) : FeedbackIntent()
    data object DismissResult : FeedbackIntent()
}

sealed interface FeedbackMode {
    data object Idle : FeedbackMode
    data object Submitting : FeedbackMode
}

sealed interface SubmitResult {
    data class Success(val hadMessage: Boolean) : SubmitResult
    data object Error : SubmitResult
}

data class ValidationError(
    val artistError: Boolean = false,
    val titleError: Boolean = false,
    val messageError: Boolean = false
)

data class FeedbackState(
    val mode: FeedbackMode = FeedbackMode.Idle,
    val validationError: ValidationError = ValidationError(),
    val submitResult: SubmitResult? = null
)

class FeedbackViewModel(
    private val songRequestService: SongRequestService
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeedbackState())
    val uiState: StateFlow<FeedbackState> = _uiState.asStateFlow()

    fun onIntent(intent: FeedbackIntent) {
        when (intent) {
            is FeedbackIntent.SubmitRequest -> submitRequest(intent.artist, intent.title, intent.message)
            FeedbackIntent.DismissResult -> dismissResult()
        }
    }

    private fun submitRequest(artist: String, title: String, message: String) {
        val artistEmpty = artist.isBlank()
        val titleEmpty = title.isBlank()
        val messageTooLong = message.length > SongRequestService.MAX_MESSAGE_LENGTH

        if (artistEmpty || titleEmpty || messageTooLong) {
            _uiState.update {
                it.copy(
                    validationError = ValidationError(
                        artistError = artistEmpty,
                        titleError = titleEmpty,
                        messageError = messageTooLong
                    )
                )
            }
            return
        }

        _uiState.update { it.copy(mode = FeedbackMode.Submitting, validationError = ValidationError()) }

        val hasMessage = message.isNotBlank()

        viewModelScope.launch {
            val request = SongRequest(
                artist = artist.trim(),
                title = title.trim(),
                message = message.trim().ifBlank { null }
            )

            songRequestService.submitSongRequest(request).fold(
                onFailure = {
                    _uiState.update {
                        it.copy(
                            mode = FeedbackMode.Idle,
                            submitResult = SubmitResult.Error
                        )
                    }
                },
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            mode = FeedbackMode.Idle,
                            submitResult = SubmitResult.Success(hasMessage)
                        )
                    }
                }
            )
        }
    }

    private fun dismissResult() {
        _uiState.update { it.copy(submitResult = null) }
    }
}
