package site.ylan.application.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import site.ylan.application.model.Task

interface TaskRepository {
    val tasks: StateFlow<List<Task>>
    fun addTask(task: Task)
    fun updateTask(task: Task)
    fun toggleTaskCompletion(id: String)
    fun deleteTask(id: String)
    fun clearCompletedTasks()
}

class InMemoryTaskRepository(
    initialTasks: List<Task> = emptyList()
) : TaskRepository {

    private val _tasks = MutableStateFlow(initialTasks)
    override val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    override fun addTask(task: Task) {
        _tasks.update { currentList ->
            currentList + task
        }
    }

    override fun updateTask(task: Task) {
        _tasks.update { currentList ->
            currentList.map { if (it.id == task.id) task else it }
        }
    }

    override fun toggleTaskCompletion(id: String) {
        _tasks.update { currentList ->
            currentList.map {
                if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
            }
        }
    }

    override fun deleteTask(id: String) {
        _tasks.update { currentList ->
            currentList.filterNot { it.id == id }
        }
    }

    override fun clearCompletedTasks() {
        _tasks.update { currentList ->
            currentList.filterNot { it.isCompleted }
        }
    }
}
