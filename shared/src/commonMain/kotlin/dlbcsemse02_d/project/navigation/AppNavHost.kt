package dlbcsemse02_d.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dlbcsemse02_d.project.presentation.feedback.FeedbackScreen
import dlbcsemse02_d.project.presentation.moderator.ModeratorScreen
import dlbcsemse02_d.project.presentation.playing.NowPlayingScreen
import dlbcsemse02_d.project.presentation.playlist.PlaylistScreen

@Composable
fun AppNavHost(modifier: Modifier) {
    val navigator = LocalNavigator.current

    NavHost(
        navController = navigator.navController,
        startDestination = NowPlaying,
        modifier = modifier
    ) {
        composable<NowPlaying> {
            NowPlayingScreen()
        }

        composable<Playlist> {
            PlaylistScreen()
        }

        composable<Feedback> {
            FeedbackScreen()
        }

        composable<Moderator> {
            ModeratorScreen()
        }
    }
}
