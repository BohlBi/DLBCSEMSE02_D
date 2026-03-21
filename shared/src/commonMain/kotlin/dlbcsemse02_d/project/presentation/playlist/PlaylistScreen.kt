package dlbcsemse02_d.project.presentation.playlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import radioapp.shared.generated.resources.Res
import radioapp.shared.generated.resources.coming_next
import radioapp.shared.generated.resources.dismiss
import radioapp.shared.generated.resources.now_playing
import radioapp.shared.generated.resources.playlist
import radioapp.shared.generated.resources.rate_playlist
import radioapp.shared.generated.resources.rating_error
import radioapp.shared.generated.resources.rating_toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaylistScreen(
    viewModel: PlaylistViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState()
    val dismissText = stringResource(Res.string.dismiss)
    val errorText = stringResource(Res.string.rating_error)
    val successText = (uiState.ratingResult as? RatingResult.Success)?.let {
        stringResource(Res.string.rating_toast, it.stars)
    }

    LaunchedEffect(uiState.ratingResult) {
        val result = uiState.ratingResult ?: return@LaunchedEffect
        val message = when (result) {
            is RatingResult.Success -> successText ?: return@LaunchedEffect
            RatingResult.Error -> errorText
        }
        snackbarHostState.showSnackbar(
            message = message,
            actionLabel = dismissText,
            duration = SnackbarDuration.Indefinite
        )
        viewModel.onIntent(PlaylistIntent.DismissRatingResult)
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
        ) {
            when (val mode = uiState.mode) {
                PlaylistMode.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is PlaylistMode.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = mode.message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                PlaylistMode.Loaded -> {
                    Text(
                        text = stringResource(Res.string.playlist),
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            SectionHeader(text = stringResource(Res.string.now_playing))
                        }

                        uiState.songs.firstOrNull()?.let { currentSong ->
                            item {
                                SongCard(song = currentSong)
                            }
                        }

                        if (uiState.songs.size > 1) {
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                SectionHeader(text = stringResource(Res.string.coming_next))
                            }

                            items(uiState.songs.drop(1)) { song ->
                                SongCard(song = song)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.onIntent(PlaylistIntent.OpenRatingSheet) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(Res.string.rate_playlist))
                    }

                }
            }
        }
    }

    if (uiState.showRatingSheet) {
        PlaylistRatingSheet(
            sheetState = sheetState,
            currentRating = uiState.rating,
            isSubmitting = uiState.isSubmittingRating,
            onSubmit = { stars ->
                viewModel.onIntent(PlaylistIntent.RatePlaylist(stars))
            },
            onDismiss = {
                viewModel.onIntent(PlaylistIntent.CloseRatingSheet)
            }
        )
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary
    )
}
