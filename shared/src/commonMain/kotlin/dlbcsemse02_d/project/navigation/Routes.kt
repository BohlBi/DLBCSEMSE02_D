package dlbcsemse02_d.project.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route

@Serializable
data object HomeRoute : Route

@Serializable
data object SettingsRoute : Route
