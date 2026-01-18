package dlbcsemse02_d.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class Navigator(internal val navController: NavHostController) {
    fun navigateTo(route: Route) {
        navController.navigate(route) {
            launchSingleTop = true
            restoreState = true
        }
    }

    @Composable
    fun getCurrentRoute(): Route? {
        val backStackEntry by navController.currentBackStackEntryAsState()
        return when (backStackEntry?.destination?.route) {
            NowPlaying::class.qualifiedName -> NowPlaying
            Playlist::class.qualifiedName -> Playlist
            Feedback::class.qualifiedName -> Feedback
            Moderator::class.qualifiedName -> Moderator
            else -> null
        }
    }
}

@Composable
fun rememberNavigator(navController: NavHostController = rememberNavController()): Navigator {
    return remember(navController) {
        Navigator(navController)
    }
}
