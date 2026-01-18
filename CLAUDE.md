# Project Architecture & Development Rules

## Project Structure

This is a Kotlin Multiplatform (KMP) project with Compose Multiplatform for Android and iOS.

```
shared/src/commonMain/kotlin/dlbcsemse02_d/project/
├── application/service/     # Application layer services
├── di/                      # Dependency injection (Koin)
├── domain/
│   ├── exception/           # Domain exceptions
│   ├── model/               # Domain models (data classes)
│   └── repository/          # Repository interfaces
├── infrastructure/
│   └── repository/          # Repository implementations
└── presentation/            # UI layer (Compose + ViewModels)
    ├── playing/             # Now Playing feature
    └── playlist/            # Playlist feature
```

## Architecture Pattern: MVI (Model-View-Intent)

Each feature follows the MVI pattern with these components:

### ViewModel Structure

```kotlin
// 1. Intent - User actions
sealed class FeatureIntent {
    data object SomeAction : FeatureIntent()
    data class ActionWithData(val data: String) : FeatureIntent()
}

// 2. Mode - UI states
sealed interface FeatureMode {
    data object Loading : FeatureMode
    data object Loaded : FeatureMode
    data class Error(val message: String) : FeatureMode
}

// 3. State - Complete UI state
data class FeatureState(
    val mode: FeatureMode = FeatureMode.Loading,
    val data: List<Item> = emptyList()
)

// 4. ViewModel
class FeatureViewModel(
    private val service: SomeService
) : ViewModel() {
    private val _uiState = MutableStateFlow(FeatureState())
    val uiState: StateFlow<FeatureState> = _uiState.asStateFlow()

    fun onIntent(intent: FeatureIntent) {
        when (intent) {
            is FeatureIntent.SomeAction -> handleAction()
        }
    }
}
```

## Composable Rules

### One Composable per File

Each Composable function should be in its own file:

```
presentation/playlist/
├── PlaylistScreen.kt        # Main screen composable
├── PlaylistViewModel.kt     # ViewModel with State/Intent/Mode
├── SongCard.kt              # Song card composable
└── StarRating.kt            # Star rating composable
```

### Screen Composables

- Use `koinViewModel()` for ViewModel injection
- Collect state with `collectAsState()`
- Handle different modes with `when` expression

```kotlin
@Composable
fun FeatureScreen(
    viewModel: FeatureViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val mode = uiState.mode) {
        FeatureMode.Loading -> LoadingContent()
        is FeatureMode.Error -> ErrorContent(mode.message)
        FeatureMode.Loaded -> MainContent(uiState)
    }
}
```

## String Resources (Internationalization)

### Location

```
shared/src/commonMain/composeResources/
├── values/strings.xml          # Default (English)
└── values-de/strings.xml       # German
```

### Format

```xml
<!-- values/strings.xml (English) -->
<string name="rate_playlist">Rate the Playlist</string>
<string name="rating_toast">You rated the playlist with %1$d stars!</string>

<!-- values-de/strings.xml (German) -->
<string name="rate_playlist">Bewerte die Playlist</string>
<string name="rating_toast">Du hast die Playlist mit %1$d Sternen bewertet!</string>
```

### Usage in Composables

```kotlin
import dlbcsemse02_d.project.shared.generated.resources.Res
import dlbcsemse02_d.project.shared.generated.resources.rate_playlist
import org.jetbrains.compose.resources.stringResource

// Simple string
Text(text = stringResource(Res.string.rate_playlist))

// String with parameters
val message = stringResource(Res.string.rating_toast, starCount)
```

## Dependency Injection (Koin)

### Module Registration

```kotlin
// AppModule.kt
val viewModelModule = module {
    viewModelOf(::NowPlayingViewModel)
    viewModelOf(::PlaylistViewModel)
}
```

## Layer Responsibilities

### Domain Layer
- **Models**: Pure data classes
- **Repository Interfaces**: Abstract contracts
- **Exceptions**: Domain-specific errors

### Infrastructure Layer
- **Repository Implementations**: Concrete implementations (e.g., MockSongRepository)

### Application Layer
- **Services**: Business logic orchestration, delegates to repositories

### Presentation Layer
- **ViewModels**: UI state management with MVI
- **Composables**: UI components, one per file
