# Skillforge 

An Android learning application built for an Android Developer assessment. 
The app features a 3-screen flow (Home → Course Detail → Lesson Player) and is built entirely with Jetpack Compose.

## 🚀 App Overview

Skillforge allows users to browse courses by category, view detailed information about a course and its instructor, and play specific lessons. The app is fully read-only and requires no authentication.

### Key Technologies
- **Language**: Kotlin
- **UI Toolkit**: Jetpack Compose (Material 3)
- **Architecture**: MVVM + Repository Pattern
- **Networking**: Retrofit2 + Kotlin Coroutines + Gson
- **Image Loading**: Coil (`coil-compose`)
- **Navigation**: Jetpack Navigation Compose

## 🏗 Architecture & Project Structure

This application adheres to the **MVVM (Model-View-ViewModel)** architectural pattern combined with a clean Repository layer. The focus was on keeping the design simple but highly maintainable, given the read-only scope of the project.

### 1. Data Layer
The data layer is responsible for fetching, parsing, and caching the remote data.
- **`SkillforgeApi`**: A simple Retrofit interface pointing to the provided raw JSON endpoint.
- **`SkillforgeRepository`**: Acts as the single source of truth. Since the provided API JSON does not include unique IDs for routing (e.g., `courseId`, `lessonId`), the repository parses the `ApiModels` and gracefully maps them into `DomainModels`, injecting dynamically generated `UUID`s. It caches this list in-memory so subsequent screens don't trigger redundant network calls.
- **Dependency Injection**: To avoid heavy frameworks like Hilt or Koin for a scoped assessment, the repository is instantiated as a Singleton in the `SkillforgeApplication` class and passed to ViewModels via a custom `ViewModelProvider.Factory`.

### 2. UI Layer
The UI is strictly built using **Jetpack Compose** (Material 3).
- **State Management**: `HomeViewModel` exposes a reactive `StateFlow<HomeUiState>` containing sealed classes (`Loading`, `Success`, `Error`). Compose screens observe this flow and recompose automatically when the data load completes.
- **Shared Components**: Reusable UI elements (`CourseCard`, `CategoryCard`, `LessonRow`, `Badges`) are abstracted into a `components/` package to ensure the screens stay clean and maintain a cohesive design system.
- **Design System**: A custom `Theme.kt`, `Color.kt`, and `Type.kt` accurately map the provided Figma/screenshot hex values (`#FAF6EF`, `#0E9C8E`), and the custom "Plus Jakarta Sans" font is bundled locally to ensure typography matches perfectly without relying on internet-dependent Google Fonts loading.

### 3. Navigation
Routing is handled through Jetpack Navigation Compose using a central `NavGraph`.
- Routes: `"home"`, `"course/{courseId}"`, and `"lesson/{courseId}/{lessonId}"`.
- The IDs mapped in the Repository are used to safely pass arguments between screens.

## 🛠 How to run

1. Clone or download this repository.
2. Open the project in **Android Studio**.
3. Let Gradle sync and download all dependencies.
4. Run the `app` module on an emulator or physical device (minSdk 24+).

> **Note**: This app does not require any API keys. It fetches its mock content from a single public endpoint.

---

## 🧪 Testing

### Automated Tests
Run `./gradlew test`. `SkillforgeRepositoryTest` verifies API-to-domain model mapping and unique ID generation for courses/lessons.

### Manual QA Checklist
To thoroughly evaluate the application, consider testing the following key scenarios:

1. **Network States & Error Handling:**
   - **Loading State:** Observe the loading spinner upon initial launch while data is being fetched.
   - **Error Recovery:** Disable your device's internet connection before launching the app to verify the custom Error View appears with a functional "Retry" button.
2. **Search Functionality:**
   - Use the search bar on the Home screen to dynamically filter the "Popular courses" list by course title or category.
3. **Navigation & State Retention:**
   - Verify the smooth transition from Home → Course Detail → Lesson Player. The shared `SkillforgeRepository` acts as a single source of truth, ensuring data is retained and passed efficiently without triggering redundant network calls during navigation.
4. **Edge-to-Edge UI:**
   - Observe how the UI draws seamlessly behind the system status bar, with the system icons dynamically adapting to light/dark colors depending on the screen's header background.

---

## 🤖 How I used AI

*This section outlines how AI tools were leveraged to build the foundation of this project.*

### Tools Used
- Gemini 3.1 Pro (via Antigravity IDE)

### Example Prompts Used
1. *Build a 3-screen learning Android app called Skillforge using Jetpack Compose, Retrofit, and Coil. Provide a complete implementation plan first before writing code.*
2. *Modify the implementation plan to use converter-gson instead of kotlinx-serialization, and bundle Plus Jakarta Sans as a local font.*
3. *Generate the complete codebase, ensuring the Repository is a shared singleton and handles mapping API models to Domain models.*

### What AI got right
- The Jetpack Compose UI implementation was highly accurate, closely matching the provided screenshots. It correctly structured the MVVM architecture and implemented an in-memory Repository that seamlessly handled mapping raw API models to Domain models with dynamically generated IDs.

### What AI got wrong (and how I fixed it)
- Initially, the AI proposed using `kotlinx-serialization` with an incorrect artifact ID, and it struggled to download the `Plus Jakarta Sans` `.ttf` font files via PowerShell due to connection issues. **Fix:** I instructed it to switch to `converter-gson`, and it successfully fetched the fonts using standard `curl` commands, integrating them smoothly into `Type.kt`.
