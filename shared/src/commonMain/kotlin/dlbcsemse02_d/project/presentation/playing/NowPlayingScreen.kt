package dlbcsemse02_d.project.presentation.playing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import radioapp.shared.generated.resources.Res
import radioapp.shared.generated.resources.now_playing
import radioapp.shared.generated.resources.pause
import radioapp.shared.generated.resources.paused
import radioapp.shared.generated.resources.play
import radioapp.shared.generated.resources.remaining
import radioapp.shared.generated.resources.seconds
import radioapp.shared.generated.resources.starting_playback
import radioapp.shared.generated.resources.try_again

@Composable
private fun SongCard(
    song: dlbcsemse02_d.project.domain.model.Song?,
    isPlaying: Boolean,
    onActionClick: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Box {
            song?.let {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(if (isPlaying) Res.string.now_playing else Res.string.paused),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = it.interpret,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = it.album,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${stringResource(Res.string.remaining)} ${it.duration.toInt()} ${
                            stringResource(
                                Res.string.seconds
                            )
                        }",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                IconButton(
                    onClick = onActionClick,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = stringResource(if (isPlaying) Res.string.pause else Res.string.play)
                    )
                }
            } ?: Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(64.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun NowPlayingScreen(
    viewModel: NowPlayingViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
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

        Spacer(modifier = Modifier.height(32.dp))
    }
}