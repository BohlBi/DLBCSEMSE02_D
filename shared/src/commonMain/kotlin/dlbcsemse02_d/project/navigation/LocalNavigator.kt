package dlbcsemse02_d.project.navigation

import androidx.compose.runtime.compositionLocalOf

val LocalNavigator = compositionLocalOf<Navigator> {
    error("Navigator not provided. Make sure AppNavHost is in the composition tree.")
}
