# Task Manager Android Application

A modern, native Android Task Manager application built with Kotlin, Jetpack Compose, and modern Android architecture practices.

## 📱 Features

- **Task Management**: Create, edit, complete, and delete tasks with custom titles and descriptions.
- **Priority System**: Assign priority levels (**Low**, **Medium**, **High**) to tasks with distinct visual chips.
- **Smart Filtering**: Filter tasks by status (**All**, **Active**, **Completed**).
- **Batch Operations**: Clear all completed tasks with one click.
- **Modern UI/UX**: Designed using **Material Design 3** featuring expressive dynamic styling, adaptive components, and **Edge-to-Edge** display support.
- **State-driven Navigation**: Built with **Jetpack Navigation 3** (`androidx.navigation3`).

## 🛠 Tech Stack & Architecture

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Navigation**: Jetpack Navigation 3 (`androidx.navigation3`)
- **Architecture**: MVVM (Model-View-ViewModel) with Unidirectional Data Flow
- **Asynchronous & State**: `StateFlow`, `SharingStarted.WhileSubscribed`, Kotlin Coroutines
- **Testing**: Unit tests with JUnit 4 for ViewModels and Repositories (`TaskViewModelTest`, `TaskRepositoryTest`)

## 📁 Project Structure

```
site.ylan.application/
├── data/
│   └── TaskRepository.kt         # In-memory repository managing task data & StateFlow stream
├── model/
│   ├── Task.kt                   # Data model for Task
│   ├── Priority.kt               # Enum for Low, Medium, High priority
│   └── TaskFilter.kt             # Enum for task filtering options
├── navigation/
│   └── TaskNavKey.kt             # Navigation 3 serializable route keys
├── ui/
│   ├── components/
│   │   ├── PriorityChip.kt       # Material 3 priority indicator chip
│   │   └── TaskItem.kt           # Task item card layout
│   ├── screens/
│   │   ├── TaskListScreen.kt     # Main task list overview & filter screen
│   │   └── AddEditTaskScreen.kt # Task creation & editing screen
│   └── theme/
│       ├── Color.kt              # Color tokens
│       ├── Theme.kt              # Material 3 Dynamic Theme setup
│       └── Type.kt               # Typography definition
├── viewmodel/
│   └── TaskViewModel.kt          # ViewModel handling business logic & UI state
└── MainActivity.kt               # Entry point wiring Edge-to-Edge & Navigation 3
```

## 🧪 Testing

Unit tests cover key business logic, task mutations, filtering behaviors, and state updates:

- `TaskRepositoryTest`: Validates initial data, task addition, editing, completion toggle, deletion, and batch operations.
- `TaskViewModelTest`: Validates `StateFlow` updates, filter changes, task CRUD actions, and reactive pipeline logic.

To run tests:
```bash
./gradlew testDebugUnitTest
```

## 🚀 Building & Running

1. Clone the repository:
   ```bash
   git clone https://github.com/pepsi-wyl/Application.git
   ```
2. Open the project in **Android Studio**.
3. Build and run the app on an Android device or emulator running **Android 8.0 (API 26)** or higher.

```bash
./gradlew assembleDebug
```
