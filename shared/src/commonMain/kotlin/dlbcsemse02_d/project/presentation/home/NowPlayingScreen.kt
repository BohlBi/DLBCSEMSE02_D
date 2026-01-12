package dlbcsemse02_d.project.presentation.home

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dlbcsemse02_d.project.navigation.LocalNavigator
import dlbcsemse02_d.project.navigation.Feedback
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
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
                Text("Wiedergabe wird gestartet", modifier = Modifier.padding(top = 16.dp))
            }

            is CurrentMode.Playing -> {
                uiState.song?.let { song ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Box {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Now Playing",
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = song.title,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Text(
                                    text = song.interpret,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = song.album,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Duration: ${song.duration.toInt()} seconds",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            IconButton(
                                onClick = { viewModel.onIntent(NowPlayingIntent.StopPlaying) },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Pause,
                                    contentDescription = "Pause"
                                )
                            }
                        }
                    }
                }
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
                    Text("Erneut versuchen")
                }
            }

            CurrentMode.Idle -> {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Box {
                        uiState.song?.let { song ->
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Now Playing",
                                    style = MaterialTheme.typography.labelMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = song.title,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Text(
                                    text = song.interpret,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = song.album,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = "Duration: ${song.duration.toInt()} seconds",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            IconButton(
                                onClick = { viewModel.onIntent(NowPlayingIntent.StartPlaying) },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play"
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
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}