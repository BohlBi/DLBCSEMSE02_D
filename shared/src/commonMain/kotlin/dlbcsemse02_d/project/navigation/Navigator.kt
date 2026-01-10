package dlbcsemse02_d.project.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class Navigator(internal val navController: NavHostController) {
    fun navigateTo(route: Route ) {
        navController.navigate(route)
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}

@Composable
fun rememberNavigator(navController: NavHostController = rememberNavController()): Navigator {
    return remember(navController) {
        Navigator(navController)
    }
}
