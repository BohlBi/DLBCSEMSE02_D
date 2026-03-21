package dlbcsemse02_d.project.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dlbcsemse02_d.project.application.service.ModeratorService
import dlbcsemse02_d.project.navigation.NowPlaying
import dlbcsemse02_d.project.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NavigationState(
    val currentRoute: Route = NowPlaying,
    val unseenCount: Int = 0
)

class NavigationViewModel(
    private val moderatorService: ModeratorService
) : ViewModel() {

    private val _uiState = MutableStateFlow(NavigationState())
    val uiState: StateFlow<NavigationState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            moderatorService.getUnseenCountFlow().collect { count ->
                _uiState.update { it.copy(unseenCount = count) }
            }
        }
    }

    fun onRouteChanged(route: Route?) {
        route?.let {
            _uiState.update { state -> state.copy(currentRoute = it) }
        }
    }
}
