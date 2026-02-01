## Radio App (Kotlin Multiplatform)

  Prototypische Radio-App mit klarer Schichtenarchitektur (UI/ViewModel -> Services -> Repositories/Stubs).
  Der Fokus liegt auf Android; der gemeinsame Code liegt in shared/commonMain.

  ## Features (User Stories)

  - US1: Aktuellen Titel anzeigen (Now Playing)
  - US2: Playlist bewerten (Sterne 1–5)
  - US3: Songwunsch senden (Nachricht optional, max. 200 Zeichen)
  - US4: Moderator:in bewerten (Sterne 1–5)
  - US5: Moderator-Dashboard mit NEU-Markierung (Bewertungen + Songwünsche)

  ## Architektur (Kurz)

  - Presentation: Compose Screens + ViewModels (StateFlow, MVI‑ähnlich)
  - Business: Services (SongService, ModeratorService)
  - Data: Repository-Interfaces + Stub-Implementierungen (Mock*Repository, MockDataStore)

  ## Project Structure

  This project uses the recommended structure for Android KMP applications with the
  com.android.kotlin.multiplatform.library plugin.

  ## Vorgehen (kurz)

  Wir sind iterativ entlang der User Stories vorgegangen:

  1. Grundgerüst (Navigation, DI, State-Management)
  2. Umsetzung der Features US1–US5
  3. Manuelle Abnahme pro User Story (Screenshots für den Bericht)
  4. Dokumentation von Annahmen und Limitationen

  Der Fortschritt wurde über Issues dokumentiert; der Prototyp entspricht dem aktuellen Stand im Repository.

  ### Modules

  #### /shared - KMP Library Module

  Contains all shared/reusable code across platforms. Uses the
  com.android.kotlin.multiplatform.library plugin.

  - commonMain/ - Platform-independent code (UI, business logic)
  - androidMain/ - Android-specific implementations

  #### /composeApp - Android Application Module

  Android app entry point. Uses the standard com.android.application plugin.

  Contains MainActivity and Android-specific configuration.

  ## Code Organization

  | What | Where | Why |
  |------|-------|-----|
  | UI Code (Composables) | shared/ | Plattformunabhängig |
  | Business Logic | shared/ | Plattformunabhängig |
  | Platform APIs | shared/ (expect/actual) | Abstraktion für Multiplatform |
  | MainActivity | composeApp/ | Android entry point |
  | App manifest | composeApp/ | Android configuration |

  ## Known Limitations (Prototype)

  - Externe Sender-APIs sind nicht angebunden (Stubs/MockDataStore).
  - Keine Persistenz (In-Memory only).
  - Fehlerpfade vorhanden, aber durch Stubs praktisch nicht auslösbar.
  - Kein Refresh im Moderator-Dashboard (lädt beim Öffnen).

  ### Build and Run Android Application

  To build and run the development version of the Android app, use the run configuration from the run widget
  in your IDE's toolbar or build it directly from the terminal:

  - on macOS/Linux

    ./gradlew :composeApp:assembleDebug
  - on Windows

    .\gradlew.bat :composeApp:assembleDebug
