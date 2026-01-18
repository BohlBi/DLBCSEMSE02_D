package dlbcsemse02_d.project.presentation.moderator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dlbcsemse02_d.project.domain.model.ModeratorRating
import dlbcsemse02_d.project.domain.model.SongRequest
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import radioapp.shared.generated.resources.Res
import radioapp.shared.generated.resources.moderator_dashboard
import radioapp.shared.generated.resources.new_badge
import radioapp.shared.generated.resources.no_ratings
import radioapp.shared.generated.resources.no_song_requests
import radioapp.shared.generated.resources.ratings_section
import radioapp.shared.generated.resources.song_requests_section
import radioapp.shared.generated.resources.stars_format

@Composable
fun ModeratorScreen(
    viewModel: ModeratorViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(Res.string.moderator_dashboard),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (val mode = uiState.mode) {
            ModeratorMode.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is ModeratorMode.Error -> {
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

            ModeratorMode.Loaded -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    SectionHeader(text = stringResource(Res.string.ratings_section))
                    Spacer(modifier = Modifier.height(8.dp))

                    if (uiState.ratings.isEmpty()) {
                        EmptyStateCard(text = stringResource(Res.string.no_ratings))
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.ratings) { rating ->
                                RatingCard(rating = rating)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    SectionHeader(text = stringResource(Res.string.song_requests_section))
                    Spacer(modifier = Modifier.height(8.dp))

                    if (uiState.songRequests.isEmpty()) {
                        EmptyStateCard(text = stringResource(Res.string.no_song_requests))
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.songRequests) { request ->
                                SongRequestCard(request = request)
                            }
                        }
                    }
                }
            }
        }
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

@Composable
private fun EmptyStateCard(text: String) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RatingCard(rating: ModeratorRating) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(rating.score) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = stringResource(Res.string.stars_format, rating.score),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (!rating.seen) {
                NewBadge()
            }
        }
    }
}

@Composable
private fun SongRequestCard(request: SongRequest) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = request.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = request.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                request.message?.let { message ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (!request.seen) {
                Spacer(modifier = Modifier.width(8.dp))
                NewBadge()
            }
        }
    }
}

@Composable
private fun NewBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.error)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = stringResource(Res.string.new_badge),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onError,
            fontWeight = FontWeight.Bold
        )
    }
}
