This is a Kotlin Multiplatform project targeting Android.

## Project Structure

This project uses the recommended structure for Android KMP applications with the new `com.android.kotlin.multiplatform.library` plugin.

### Modules

#### `/shared` - KMP Library Module
Contains all shared/reusable code across platforms. Uses the `com.android.kotlin.multiplatform.library` plugin.

- `commonMain/` - Platform-independent code (UI, business logic)
- `androidMain/` - Android-specific implementations

#### `/composeApp` - Android Application Module
Android app entry point. Uses the standard `com.android.application` plugin.

Contains MainActivity and Android-specific configuration.

## Code Organization

| What | Where | Why |
|------|-------|-----|
| UI Code (Composables) | `shared/` | Can be reused for iOS/Desktop later |
| Business Logic | `shared/` | Platform-independent |
| Platform APIs | `shared/` (expect/actual) | Abstracted for multiplatform |
| MainActivity | `composeApp/` | Android-specific entry point |
| App manifest | `composeApp/` | Android-specific configuration |

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE's toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html) and the [Android KMP Plugin](https://developer.android.com/kotlin/multiplatform/plugin)
