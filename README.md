This is a Kotlin Multiplatform project targeting Android.

## Project Structure
This project uses the recommended structure for Android KMP applications with the new `com.android.kotlin.multiplatform.library` plugin.

## Work & Tracking
Wir steuern das Projekt über GitHub Issues. Jede Aufgabe ist ein Issue, damit Fortschritte und Entscheidungen und nachvollziehbar sind.

### Epic-Konzept (E0, E1, E2, ...)
Ein "Epic" (E) ist ein grober Arbeitsblock, der aus mehreren kleineren Issues besteht. Wir nutzen Epics, um das Projekt in verständliche Bereiche zu zerlegen und den Fortschritt sauber zu tracken.

**Kennzeichnung:**
- Issue-Titel starten mit `(E0)`, `(E1)`, `(E2)` ...

### Epic-Übersicht

- **E0 – Orga, Tutor-Freigabe, Bericht & Compliance**
  - Ziel: Projekt formal sauber starten und Bericht/Abgabe-Prozess absichern.
  - Warum: Verhindert späte Formalia-Probleme (Titelblatt, Kapitelzuordnung, Abgabe, Annahmen).

- **E1 – Anforderungen & Annahmen**
  - Ziel: User Stories, Akzeptanzkriterien und Annahmen definieren und dokumentieren.
  - Warum: Ohne Stakeholder müssen Annahmen sauber festgehalten werden; daraus leiten wir Scope und Tests ab.

- **E2 – Setup & Architektur (DDD-light)**
  - Ziel: Projektstruktur, Layering (Domain/Data/UI), Navigation, CI und technische Entscheidungen.
  - Warum: Stabiler Unterbau für Features, testbare Use Cases und wartbare Struktur.

- **E3 – Systemschnittstellen (Stubs)**
  - Ziel: Alle Schnittstellen zu Sender-Systemen als Stubs implementieren (Pflicht in der Aufgabenstellung).
  - Warum: Das ist ein expliziter Prüfungsanforderungspunkt und wird oft übersehen.

- **E4 – Listener Features (MVP)**
  - Ziel: Kernfunktionen für Hörer:innen (Now Playing, Playlist, Bewertungen, Songwunsch).
  - Warum: Deckt die zentralen User Stories ab und liefert schnell ein demonstrierbares MVP.

- **E5 – Moderator Features ("sofort informiert")**
  - Ziel: Moderator-Dashboard / Live-Feedback-Mechanik (Polling/Simulation) für eingehende Bewertungen.
  - Warum: User Story "Moderator:in wird sofort informiert" muss sichtbar erfüllt sein.

- **E6 – Persistenz & Offline-Verhalten**
  - Ziel: Caching/Outbox/History lokal speichern und Offline-Verhalten definieren.
  - Warum: Erhöht Robustheit und Nachvollziehbarkeit; gut für Qualität im Bericht.

- **E7 – UX & Quality**
  - Ziel: Loading/Error/Empty States, Accessibility, Konsistenz und Edge Cases.
  - Warum: Bewertungsfaktor "Qualität" lässt sich hier sauber belegen.

- **E8 – Tests & QA**
  - Ziel: Unit Tests für Use Cases + mindestens ein UI/Flow-Test + manuelle Smoke-Test-Checkliste.
  - Warum: Belegt Qualität und reduziert Risiko vor Abgabe.



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
