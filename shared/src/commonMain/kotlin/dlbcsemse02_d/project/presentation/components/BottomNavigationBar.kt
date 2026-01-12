package dlbcsemse02_d.project.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.QueueMusic
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dlbcsemse02_d.project.navigation.Feedback
import dlbcsemse02_d.project.navigation.LocalNavigator
import dlbcsemse02_d.project.navigation.NowPlaying
import dlbcsemse02_d.project.navigation.Playlist
import dlbcsemse02_d.project.navigation.Rating

@Composable
fun BottomNavigationBar() {
    val navigator = LocalNavigator.current
    val currentRoute = navigator.getCurrentRoute()
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Now Playing") },
            label = { Text("Now Playing") },
            selected = currentRoute is NowPlaying,
            onClick = { navigator.navigateTo(NowPlaying) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.QueueMusic, contentDescription = "Playlist") },
            label = { Text("Playlist") },
            selected = currentRoute is Playlist,
            onClick = { navigator.navigateTo(Playlist) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Feedback, contentDescription = "Feedback") },
            label = { Text("Feedback") },
            selected = currentRoute is Feedback,
            onClick = { navigator.navigateTo(Feedback) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Star, contentDescription = "Rating") },
            label = { Text("Rating") },
            selected = currentRoute is Rating,
            onClick = { navigator.navigateTo(Rating) }
        )
    }
}
