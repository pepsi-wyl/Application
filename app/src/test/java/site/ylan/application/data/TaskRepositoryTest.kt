package site.ylan.application.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import site.ylan.application.model.Priority
import site.ylan.application.model.Task

class TaskRepositoryTest {

    private lateinit var repository: TaskRepository

    @Before
    fun setUp() {
        repository = InMemoryTaskRepository()
    }

    @Test
    fun repository_initialStateIsEmpty() {
        assertTrue(repository.tasks.value.isEmpty())
    }

    @Test
    fun repository_addTask_addsTaskToList() {
        val task = Task(title = "Test Task", description = "Test Description", priority = Priority.HIGH)
        repository.addTask(task)

        val currentTasks = repository.tasks.value
        assertEquals(1, currentTasks.size)
        assertEquals(task, currentTasks.first())
    }

    @Test
    fun repository_updateTask_modifiesExistingTask() {
        val task = Task(title = "Initial Title", description = "Initial Desc")
        repository.addTask(task)

        val updated = task.copy(title = "Updated Title", priority = Priority.HIGH)
        repository.updateTask(updated)

        val currentTasks = repository.tasks.value
        assertEquals(1, currentTasks.size)
        assertEquals("Updated Title", currentTasks.first().title)
        assertEquals(Priority.HIGH, currentTasks.first().priority)
    }

    @Test
    fun repository_toggleTaskCompletion_togglesState() {
        val task = Task(title = "Task", isCompleted = false)
        repository.addTask(task)

        repository.toggleTaskCompletion(task.id)
        assertTrue(repository.tasks.value.first().isCompleted)

        repository.toggleTaskCompletion(task.id)
        assertFalse(repository.tasks.value.first().isCompleted)
    }

    @Test
    fun repository_deleteTask_removesTask() {
        val task1 = Task(title = "Task 1")
        val task2 = Task(title = "Task 2")
        repository.addTask(task1)
        repository.addTask(task2)

        repository.deleteTask(task1.id)

        val currentTasks = repository.tasks.value
        assertEquals(1, currentTasks.size)
        assertEquals(task2.id, currentTasks.first().id)
    }

    @Test
    fun repository_clearCompletedTasks_removesOnlyCompleted() {
        val task1 = Task(title = "Active Task", isCompleted = false)
        val task2 = Task(title = "Completed Task", isCompleted = true)
        repository.addTask(task1)
        repository.addTask(task2)

        repository.clearCompletedTasks()

        val currentTasks = repository.tasks.value
        assertEquals(1, currentTasks.size)
        assertEquals(task1.id, currentTasks.first().id)
    }
}
