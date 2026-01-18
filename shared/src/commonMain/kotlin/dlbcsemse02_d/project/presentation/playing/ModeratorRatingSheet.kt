package dlbcsemse02_d.project.presentation.playing

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dlbcsemse02_d.project.domain.model.Moderator
import dlbcsemse02_d.project.presentation.playlist.StarRating
import org.jetbrains.compose.resources.stringResource
import radioapp.shared.generated.resources.Res
import radioapp.shared.generated.resources.current_moderator
import radioapp.shared.generated.resources.no_moderator
import radioapp.shared.generated.resources.rate_moderator
import radioapp.shared.generated.resources.rating_1
import radioapp.shared.generated.resources.rating_2
import radioapp.shared.generated.resources.rating_3
import radioapp.shared.generated.resources.rating_4
import radioapp.shared.generated.resources.rating_5
import radioapp.shared.generated.resources.select_rating
import radioapp.shared.generated.resources.submit_rating

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModeratorRatingSheet(
    sheetState: SheetState,
    moderator: Moderator?,
    isLoading: Boolean,
    isSubmitting: Boolean,
    showValidationError: Boolean,
    onSubmit: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedRating by remember { mutableStateOf<Int?>(null) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(Res.string.rate_moderator),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(24.dp))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                moderator == null -> {
                    Text(
                        text = stringResource(Res.string.no_moderator),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                else -> {
                    Text(
                        text = stringResource(Res.string.current_moderator, moderator.name),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    StarRating(
                        rating = selectedRating ?: 0,
                        onRatingChanged = { selectedRating = it }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    selectedRating?.let { rating ->
                        Text(
                            text = getRatingLabel(rating),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (showValidationError && selectedRating == null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(Res.string.select_rating),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            selectedRating?.let { onSubmit(it) }
                        },
                        enabled = !isSubmitting && selectedRating != null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(stringResource(Res.string.submit_rating))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun getRatingLabel(rating: Int): String {
    return when (rating) {
        1 -> stringResource(Res.string.rating_1)
        2 -> stringResource(Res.string.rating_2)
        3 -> stringResource(Res.string.rating_3)
        4 -> stringResource(Res.string.rating_4)
        5 -> stringResource(Res.string.rating_5)
        else -> ""
    }
}
