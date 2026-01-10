package dlbcsemse02_d.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dlbcsemse02_d.project.presentation.home.HomeScreen
import dlbcsemse02_d.project.presentation.settings.SettingsScreen

@Composable
fun AppNavHost(modifier: Modifier) {
    val navigator = rememberNavigator()

    CompositionLocalProvider(LocalNavigator provides navigator) {
        NavHost(
            navController = navigator.navController,
            startDestination = HomeRoute,
            modifier = modifier
        ) {
            composable<HomeRoute> {
                HomeScreen()
            }

            composable<SettingsRoute> {
                SettingsScreen()
            }
        }
    }
}
