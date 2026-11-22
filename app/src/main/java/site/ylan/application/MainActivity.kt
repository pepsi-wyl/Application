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
import site.ylan.application.ui.theme.ApplicationTheme
import site.ylan.application.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {

    private val taskViewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskManagerApp(viewModel = taskViewModel)
                }
            }
        }
    }
}

@Composable
fun TaskManagerApp(
    viewModel: TaskViewModel = viewModel()
) {
    val tasks by viewModel.tasks.collectAsStateWithLifecycle()
    val allTasks by viewModel.allTasks.collectAsStateWithLifecycle()
    val currentFilter by viewModel.filter.collectAsStateWithLifecycle()

    val backStack = rememberNavBackStack(TaskNavKey.TaskList)

    NavDisplay(
        backStack = backStack,
        onBack = {
            if (backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
            }
        },
        entryProvider = { key ->
            when (key) {
                is TaskNavKey.TaskList -> NavEntry(key) {
                    TaskListScreen(
                        tasks = tasks,
                        allTasks = allTasks,
                        currentFilter = currentFilter,
                        onFilterSelected = viewModel::setTaskFilter,
                        onToggleTaskCompletion = viewModel::toggleTaskCompletion,
                        onEditTask = { task ->
                            backStack.add(TaskNavKey.AddEditTask(task.id))
                        },
                        onDeleteTask = viewModel::deleteTask,
                        onClearCompleted = viewModel::clearCompletedTasks,
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
                                viewModel.addTask(title, description, priority)
                            } else {
                                viewModel.editTask(taskToEdit.id, title, description, priority)
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
