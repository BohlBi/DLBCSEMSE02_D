package dlbcsemse02_d.project.presentation.playing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import radioapp.shared.generated.resources.Res
import radioapp.shared.generated.resources.dismiss
import radioapp.shared.generated.resources.like_what_you_hear
import radioapp.shared.generated.resources.moderator_rating_error
import radioapp.shared.generated.resources.moderator_rating_success
import radioapp.shared.generated.resources.select_rating
import radioapp.shared.generated.resources.starting_playback
import radioapp.shared.generated.resources.try_again

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingScreen(
    viewModel: NowPlayingViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState()
    var showValidationError by remember { mutableStateOf(false) }

    val dismissText = stringResource(Res.string.dismiss)
    val successText = stringResource(Res.string.moderator_rating_success)
    val errorText = stringResource(Res.string.moderator_rating_error)
    val selectRatingText = stringResource(Res.string.select_rating)

    LaunchedEffect(uiState.ratingResult) {
        val result = uiState.ratingResult ?: return@LaunchedEffect
        showValidationError = false
        val message = when (result) {
            ModeratorRatingResult.Success -> {
                viewModel.onIntent(NowPlayingIntent.CloseModeratorRating)
                successText
            }
            ModeratorRatingResult.Error -> errorText
            ModeratorRatingResult.NoRatingSelected -> {
                showValidationError = true
                selectRatingText
            }
        }
        if (result != ModeratorRatingResult.NoRatingSelected) {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = dismissText,
                duration = SnackbarDuration.Short
            )
        }
        viewModel.onIntent(NowPlayingIntent.DismissRatingResult)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (val state = uiState.mode) {
                is CurrentMode.Loading -> {
                    CircularProgressIndicator()
                    Text(
                        stringResource(Res.string.starting_playback),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                is CurrentMode.Playing -> {
                    SongCard(
                        song = uiState.song,
                        isPlaying = true,
                        onActionClick = { viewModel.onIntent(NowPlayingIntent.StopPlaying) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                is CurrentMode.Error -> {
                    Text(
                        text = state.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        viewModel.onIntent(NowPlayingIntent.StartPlaying)
                    }) {
                        Text(stringResource(Res.string.try_again))
                    }
                }

                CurrentMode.Idle -> {
                    SongCard(
                        song = uiState.song,
                        isPlaying = false,
                        onActionClick = { viewModel.onIntent(NowPlayingIntent.StartPlaying) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { viewModel.onIntent(NowPlayingIntent.OpenModeratorRating) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.like_what_you_hear))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (uiState.showModeratorSheet) {
        ModeratorRatingSheet(
            sheetState = sheetState,
            moderator = uiState.moderator,
            isLoading = uiState.isLoadingModerator,
            isSubmitting = uiState.isSubmittingRating,
            showValidationError = showValidationError,
            onSubmit = { score ->
                showValidationError = false
                viewModel.onIntent(NowPlayingIntent.SubmitModeratorRating(score))
            },
            onDismiss = {
                showValidationError = false
                viewModel.onIntent(NowPlayingIntent.CloseModeratorRating)
            }
        )
    }
}
