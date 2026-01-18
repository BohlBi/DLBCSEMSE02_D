package dlbcsemse02_d.project.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.QueueMusic
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import dlbcsemse02_d.project.navigation.Feedback
import dlbcsemse02_d.project.navigation.LocalNavigator
import dlbcsemse02_d.project.navigation.NowPlaying
import dlbcsemse02_d.project.navigation.Playlist
import dlbcsemse02_d.project.navigation.Moderator
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import radioapp.shared.generated.resources.Res
import radioapp.shared.generated.resources.nav_feedback
import radioapp.shared.generated.resources.nav_moderator
import radioapp.shared.generated.resources.nav_now_playing
import radioapp.shared.generated.resources.nav_playlist

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(
    viewModel: NavigationViewModel = koinViewModel()
) {
    val navigator = LocalNavigator.current
    val observedRoute = navigator.getCurrentRoute()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(observedRoute) {
        viewModel.onRouteChanged(observedRoute)
    }

    val currentRoute = uiState.currentRoute

    val nowPlayingLabel = stringResource(Res.string.nav_now_playing)
    val playlistLabel = stringResource(Res.string.nav_playlist)
    val feedbackLabel = stringResource(Res.string.nav_feedback)
    val moderatorLabel = stringResource(Res.string.nav_moderator)

    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = nowPlayingLabel) },
            label = { Text(nowPlayingLabel) },
            selected = currentRoute == NowPlaying,
            onClick = { navigator.navigateTo(NowPlaying) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.AutoMirrored.Filled.QueueMusic, contentDescription = playlistLabel) },
            label = { Text(playlistLabel) },
            selected = currentRoute == Playlist,
            onClick = { navigator.navigateTo(Playlist) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Feedback, contentDescription = feedbackLabel) },
            label = { Text(feedbackLabel) },
            selected = currentRoute == Feedback,
            onClick = { navigator.navigateTo(Feedback) }
        )
        NavigationBarItem(
            icon = {
                Box {
                    Icon(Icons.Filled.Person, contentDescription = moderatorLabel)
                    if (uiState.unseenCount > 0) {
                        Badge(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 6.dp, y = (-6).dp)
                        ) {
                            Text(uiState.unseenCount.toString())
                        }
                    }
                }
            },
            label = { Text(moderatorLabel) },
            selected = currentRoute == Moderator,
            onClick = { navigator.navigateTo(Moderator) }
        )
    }
}
