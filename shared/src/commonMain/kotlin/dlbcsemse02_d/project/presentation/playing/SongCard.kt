package dlbcsemse02_d.project.presentation.playing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dlbcsemse02_d.project.domain.model.Song
import org.jetbrains.compose.resources.stringResource
import radioapp.shared.generated.resources.Res
import radioapp.shared.generated.resources.now_playing
import radioapp.shared.generated.resources.pause
import radioapp.shared.generated.resources.paused
import radioapp.shared.generated.resources.play
import radioapp.shared.generated.resources.remaining
import radioapp.shared.generated.resources.seconds

@Composable
fun SongCard(
    song: Song?,
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
                            stringResource(Res.string.seconds)
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
