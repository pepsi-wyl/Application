package site.ylan.application.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import site.ylan.application.data.InMemoryTaskRepository
import site.ylan.application.data.TaskRepository
import site.ylan.application.model.Priority
import site.ylan.application.model.Task
import site.ylan.application.model.TaskFilter

class TaskViewModel(
    private val repository: TaskRepository = InMemoryTaskRepository()
) : ViewModel() {

    private val _filter = MutableStateFlow(TaskFilter.ALL)
    val filter: StateFlow<TaskFilter> = _filter.asStateFlow()

    val allTasks: StateFlow<List<Task>> = repository.tasks

    val tasks: StateFlow<List<Task>> = combine(repository.tasks, _filter) { taskList, currentFilter ->
        when (currentFilter) {
            TaskFilter.ALL -> taskList
            TaskFilter.ACTIVE -> taskList.filter { !it.isCompleted }
            TaskFilter.COMPLETED -> taskList.filter { it.isCompleted }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setTaskFilter(filter: TaskFilter) {
        _filter.value = filter
    }

    fun setFilter(filter: TaskFilter) {
        setTaskFilter(filter)
    }

    fun addTask(title: String, description: String = "", priority: Priority = Priority.MEDIUM) {
        if (title.isBlank()) return
        val newTask = Task(
            title = title.trim(),
            description = description.trim(),
            priority = priority
        )
        repository.addTask(newTask)
    }

    fun addTask(task: Task) {
        repository.addTask(task)
    }

    fun editTask(id: String, title: String, description: String, priority: Priority) {
        val existingTask = repository.tasks.value.find { it.id == id } ?: return
        val updatedTask = existingTask.copy(
            title = title.trim(),
            description = description.trim(),
            priority = priority
        )
        repository.updateTask(updatedTask)
    }

    fun updateTask(task: Task) {
        repository.updateTask(task)
    }

    fun toggleTaskCompletion(id: String) {
        repository.toggleTaskCompletion(id)
    }

    fun deleteTask(id: String) {
        repository.deleteTask(id)
    }

    fun clearCompletedTasks() {
        repository.clearCompletedTasks()
    }
}
