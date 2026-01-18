package dlbcsemse02_d.project.presentation.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dlbcsemse02_d.project.application.service.SongRequestService
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import radioapp.shared.generated.resources.Res
import radioapp.shared.generated.resources.artist
import radioapp.shared.generated.resources.artist_required
import radioapp.shared.generated.resources.characters_remaining
import radioapp.shared.generated.resources.dismiss
import radioapp.shared.generated.resources.message_optional
import radioapp.shared.generated.resources.message_too_long
import radioapp.shared.generated.resources.request_error
import radioapp.shared.generated.resources.request_success
import radioapp.shared.generated.resources.request_success_with_message
import radioapp.shared.generated.resources.send_request
import radioapp.shared.generated.resources.song_request
import radioapp.shared.generated.resources.song_title
import radioapp.shared.generated.resources.title_required

@Composable
fun FeedbackScreen(
    viewModel: FeedbackViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    var artist by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    val dismissText = stringResource(Res.string.dismiss)
    val successText = stringResource(Res.string.request_success)
    val successWithMessageText = stringResource(Res.string.request_success_with_message)
    val errorText = stringResource(Res.string.request_error)

    LaunchedEffect(uiState.submitResult) {
        val result = uiState.submitResult ?: return@LaunchedEffect
        val snackbarMessage = when (result) {
            is SubmitResult.Success -> {
                artist = ""
                title = ""
                message = ""
                if (result.hadMessage) successWithMessageText else successText
            }
            SubmitResult.Error -> errorText
        }
        snackbarHostState.showSnackbar(
            message = snackbarMessage,
            actionLabel = dismissText,
            duration = SnackbarDuration.Indefinite
        )
        viewModel.onIntent(FeedbackIntent.DismissResult)
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = stringResource(Res.string.song_request),
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            ArtistTextField(
                value = artist,
                isError = uiState.validationError.artistError,
                enabled = uiState.mode != FeedbackMode.Submitting,
                onValueChange = { artist = it }
            )

            TitleTextField(
                value = title,
                isError = uiState.validationError.titleError,
                enabled = uiState.mode != FeedbackMode.Submitting,
                onValueChange = { title = it }
            )

            MessageTextField(
                value = message,
                isError = uiState.validationError.messageError,
                enabled = uiState.mode != FeedbackMode.Submitting,
                onValueChange = { message = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.onIntent(FeedbackIntent.SubmitRequest(artist, title, message))
                },
                enabled = uiState.mode != FeedbackMode.Submitting,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.mode == FeedbackMode.Submitting) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.height(24.dp)
                    )
                } else {
                    Text(stringResource(Res.string.send_request))
                }
            }
        }
    }
}

@Composable
private fun ArtistTextField(
    value: String,
    isError: Boolean,
    enabled: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(Res.string.artist)) },
        isError = isError,
        enabled = enabled,
        singleLine = true,
        supportingText = if (isError) {
            { Text(stringResource(Res.string.artist_required)) }
        } else null,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun TitleTextField(
    value: String,
    isError: Boolean,
    enabled: Boolean,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(Res.string.song_title)) },
        isError = isError,
        enabled = enabled,
        singleLine = true,
        supportingText = if (isError) {
            { Text(stringResource(Res.string.title_required)) }
        } else null,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun MessageTextField(
    value: String,
    isError: Boolean,
    enabled: Boolean,
    onValueChange: (String) -> Unit
) {
    val remainingChars = SongRequestService.MAX_MESSAGE_LENGTH - value.length

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(Res.string.message_optional)) },
        isError = isError,
        enabled = enabled,
        minLines = 3,
        maxLines = 5,
        supportingText = {
            if (isError) {
                Text(
                    text = stringResource(Res.string.message_too_long, SongRequestService.MAX_MESSAGE_LENGTH),
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text(stringResource(Res.string.characters_remaining, remainingChars))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
