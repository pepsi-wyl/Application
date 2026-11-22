package site.ylan.application.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.DeleteSweep
import androidx.compose.material.icons.outlined.TaskAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import site.ylan.application.model.Priority
import site.ylan.application.model.Task
import site.ylan.application.model.TaskFilter
import site.ylan.application.ui.components.TaskItem
import site.ylan.application.ui.theme.ApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    tasks: List<Task>,
    allTasks: List<Task>,
    currentFilter: TaskFilter,
    onFilterSelected: (TaskFilter) -> Unit,
    onToggleTaskCompletion: (String) -> Unit,
    onEditTask: (Task) -> Unit,
    onDeleteTask: (String) -> Unit,
    onClearCompleted: () -> Unit,
    onAddTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showClearCompletedConfirmation by remember { mutableStateOf(false) }
    val hasCompletedTasks = allTasks.any { it.isCompleted }

    val allCount = allTasks.size
    val activeCount = allTasks.count { !it.isCompleted }
    val completedCount = allTasks.count { it.isCompleted }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Task Manager",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = { showClearCompletedConfirmation = true },
                        enabled = hasCompletedTasks
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.DeleteSweep,
                            contentDescription = "Clear completed tasks",
                            tint = if (hasCompletedTasks) {
                                MaterialTheme.colorScheme.onSurface
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onAddTask,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null
                    )
                },
                text = { Text("Add Task") }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Filter Selector
            val filters = TaskFilter.entries.toTypedArray()
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                filters.forEachIndexed { index, filter ->
                    val isSelected = filter == currentFilter
                    val count = when (filter) {
                        TaskFilter.ALL -> allCount
                        TaskFilter.ACTIVE -> activeCount
                        TaskFilter.COMPLETED -> completedCount
                    }
                    val labelText = when (filter) {
                        TaskFilter.ALL -> "All ($count)"
                        TaskFilter.ACTIVE -> "Active ($count)"
                        TaskFilter.COMPLETED -> "Done ($count)"
                    }

                    SegmentedButton(
                        selected = isSelected,
                        onClick = { onFilterSelected(filter) },
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = filters.size)
                    ) {
                        Text(text = labelText)
                    }
                }
            }

            if (tasks.isEmpty()) {
                EmptyTaskState(
                    filter = currentFilter,
                    onAddTask = onAddTask,
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1f)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = tasks, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            onToggleCompletion = { onToggleTaskCompletion(task.id) },
                            onEdit = { onEditTask(task) },
                            onDelete = { onDeleteTask(task.id) }
                        )
                    }
                }
            }
        }
    }

    if (showClearCompletedConfirmation) {
        AlertDialog(
            onDismissRequest = { showClearCompletedConfirmation = false },
            title = { Text("Clear Completed Tasks") },
            text = { Text("Are you sure you want to remove all $completedCount completed tasks?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showClearCompletedConfirmation = false
                        onClearCompleted()
                    }
                ) {
                    Text("Clear", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearCompletedConfirmation = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun EmptyTaskState(
    filter: TaskFilter,
    onAddTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val (icon, title, message) = when (filter) {
                TaskFilter.ALL -> Triple(
                    Icons.Outlined.CheckCircleOutline,
                    "No Tasks Found",
                    "You don't have any tasks yet. Tap 'Add Task' to create your first task!"
                )
                TaskFilter.ACTIVE -> Triple(
                    Icons.Outlined.TaskAlt,
                    "No Active Tasks",
                    "All caught up! You have completed all your tasks."
                )
                TaskFilter.COMPLETED -> Triple(
                    Icons.Outlined.CheckCircleOutline,
                    "No Completed Tasks",
                    "None of your tasks are marked as completed yet."
                )
            }

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            if (filter == TaskFilter.ALL) {
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onAddTask) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Create Task")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskListScreenPreview() {
    val sampleTasks = listOf(
        Task(id = "1", title = "Complete Task 2 UI implementation", description = "Build Jetpack Compose screens", priority = Priority.HIGH, isCompleted = false),
        Task(id = "2", title = "Write Unit Tests", description = "Test ViewModel and repository logic", priority = Priority.MEDIUM, isCompleted = true),
        Task(id = "3", title = "Review PRs", description = "", priority = Priority.LOW, isCompleted = false)
    )

    ApplicationTheme {
        TaskListScreen(
            tasks = sampleTasks,
            allTasks = sampleTasks,
            currentFilter = TaskFilter.ALL,
            onFilterSelected = {},
            onToggleTaskCompletion = {},
            onEditTask = {},
            onDeleteTask = {},
            onClearCompleted = {},
            onAddTask = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TaskListScreenEmptyPreview() {
    ApplicationTheme {
        TaskListScreen(
            tasks = emptyList(),
            allTasks = emptyList(),
            currentFilter = TaskFilter.ALL,
            onFilterSelected = {},
            onToggleTaskCompletion = {},
            onEditTask = {},
            onDeleteTask = {},
            onClearCompleted = {},
            onAddTask = {}
        )
    }
}
