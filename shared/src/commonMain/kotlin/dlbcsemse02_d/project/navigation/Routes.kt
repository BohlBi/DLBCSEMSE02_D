package dlbcsemse02_d.project.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route

@Serializable
data object NowPlaying : Route

@Serializable
data object Playlist : Route

@Serializable
data object Feedback : Route

@Serializable
data object Moderator : Route
