package site.ylan.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import site.ylan.application.navigation.TaskNavKey
import site.ylan.application.ui.screens.AddEditTaskScreen
import site.ylan.application.ui.screens.TaskListScreen
import site.ylan.application.ui.screens.auth.LoginScreen
import site.ylan.application.ui.screens.auth.SignUpScreen
import site.ylan.application.ui.screens.auth.WelcomeScreen
import site.ylan.application.ui.theme.ApplicationTheme
import site.ylan.application.viewmodel.AuthViewModel
import site.ylan.application.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {

    private val taskViewModel: TaskViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskManagerApp(
                        taskViewModel = taskViewModel,
                        authViewModel = authViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun TaskManagerApp(
    taskViewModel: TaskViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val tasks by taskViewModel.tasks.collectAsStateWithLifecycle()
    val allTasks by taskViewModel.allTasks.collectAsStateWithLifecycle()
    val currentFilter by taskViewModel.filter.collectAsStateWithLifecycle()
    val authUiState by authViewModel.uiState.collectAsStateWithLifecycle()

    val backStack = rememberNavBackStack(TaskNavKey.Welcome)

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
            }
        },
        entryProvider = { key ->
            when (key) {
                is TaskNavKey.Welcome -> NavEntry(key) {
                    WelcomeScreen(
                        onNavigateToSignUp = {
                            authViewModel.resetForm()
                            backStack.add(TaskNavKey.SignUp)
                        },
                        onNavigateToLogin = {
                            authViewModel.resetForm()
                            backStack.add(TaskNavKey.Login)
                        },
                        onNavigateToTaskManager = {
                            backStack.add(TaskNavKey.TaskList)
                        }
                    )
                }

                is TaskNavKey.Login -> NavEntry(key) {
                    LoginScreen(
                        email = authUiState.email,
                        onEmailChange = authViewModel::onEmailChanged,
                        password = authUiState.password,
                        onPasswordChange = authViewModel::onPasswordChanged,
                        authError = authUiState.authError,
                        onLogin = {
                            authViewModel.login {
                                backStack.add(TaskNavKey.TaskList)
                            }
                        },
                        onGoogleSignIn = {
                            authViewModel.googleSignIn {
                                backStack.add(TaskNavKey.TaskList)
                            }
                        },
                        onNavigateToSignUp = {
                            authViewModel.resetForm()
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            }
                            backStack.add(TaskNavKey.SignUp)
                        },
                        onBack = {
                            authViewModel.clearError()
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        }
                    )
                }

                is TaskNavKey.SignUp -> NavEntry(key) {
                    SignUpScreen(
                        name = authUiState.name,
                        onNameChange = authViewModel::onNameChanged,
                        email = authUiState.email,
                        onEmailChange = authViewModel::onEmailChanged,
                        password = authUiState.password,
                        onPasswordChange = authViewModel::onPasswordChanged,
                        confirmPassword = authUiState.confirmPassword,
                        onConfirmPasswordChange = authViewModel::onConfirmPasswordChanged,
                        authError = authUiState.authError,
                        onSignUp = {
                            authViewModel.signUp {
                                backStack.add(TaskNavKey.TaskList)
                            }
                        },
                        onNavigateToLogin = {
                            authViewModel.resetForm()
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            }
                            backStack.add(TaskNavKey.Login)
                        },
                        onBack = {
                            authViewModel.clearError()
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        }
                    )
                }

                is TaskNavKey.TaskList -> NavEntry(key) {
                    TaskListScreen(
                        tasks = tasks,
                        allTasks = allTasks,
                        currentFilter = currentFilter,
                        onFilterSelected = taskViewModel::setTaskFilter,
                        onToggleTaskCompletion = taskViewModel::toggleTaskCompletion,
                        onEditTask = { task ->
                            backStack.add(TaskNavKey.AddEditTask(task.id))
                        },
                        onDeleteTask = taskViewModel::deleteTask,
                        onClearCompleted = taskViewModel::clearCompletedTasks,
                        onAddTask = {
                            backStack.add(TaskNavKey.AddEditTask(null))
                        }
                    )
                }

                is TaskNavKey.AddEditTask -> NavEntry(key) {
                    val taskToEdit = key.taskId?.let { id ->
                        allTasks.find { it.id == id }
                    }
                    AddEditTaskScreen(
                        task = taskToEdit,
                        onSave = { title, description, priority ->
                            if (taskToEdit == null) {
                                taskViewModel.addTask(title, description, priority)
                            } else {
                                taskViewModel.editTask(taskToEdit.id, title, description, priority)
                            }
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        },
                        onBack = {
                            if (backStack.size > 1) {
                                backStack.removeAt(backStack.lastIndex)
                            }
                        }
                    )
                }

                else -> error("Unknown key: $key")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TaskManagerAppPreview() {
    ApplicationTheme {
        TaskManagerApp()
    }
}
