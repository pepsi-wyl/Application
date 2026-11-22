package site.ylan.application.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import site.ylan.application.data.InMemoryTaskRepository
import site.ylan.application.model.Priority
import site.ylan.application.model.Task
import site.ylan.application.model.TaskFilter

@OptIn(ExperimentalCoroutinesApi::class)
class TaskViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: InMemoryTaskRepository
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = InMemoryTaskRepository()
        viewModel = TaskViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun viewModel_initialFilterIsAll() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.tasks.collect {} }
        assertEquals(TaskFilter.ALL, viewModel.filter.value)
    }

    @Test
    fun viewModel_addTask_updatesTaskList() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.tasks.collect {} }

        viewModel.addTask(title = "New Task", description = "Details", priority = Priority.HIGH)

        val tasks = viewModel.tasks.value
        assertEquals(1, tasks.size)
        assertEquals("New Task", tasks.first().title)
        assertEquals("Details", tasks.first().description)
        assertEquals(Priority.HIGH, tasks.first().priority)
    }

    @Test
    fun viewModel_addTask_ignoresBlankTitle() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.tasks.collect {} }

        viewModel.addTask(title = "   ", description = "Blank title")
        assertTrue(viewModel.tasks.value.isEmpty())
    }

    @Test
    fun viewModel_editTask_updatesTaskDetails() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.tasks.collect {} }

        val task = Task(title = "Original Title")
        viewModel.addTask(task)

        viewModel.editTask(
            id = task.id,
            title = "Edited Title",
            description = "Edited Desc",
            priority = Priority.LOW
        )

        val updatedTasks = viewModel.tasks.value
        assertEquals(1, updatedTasks.size)
        assertEquals("Edited Title", updatedTasks.first().title)
        assertEquals("Edited Desc", updatedTasks.first().description)
        assertEquals(Priority.LOW, updatedTasks.first().priority)
    }

    @Test
    fun viewModel_toggleTaskCompletion_togglesState() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.tasks.collect {} }

        val task = Task(title = "Task", isCompleted = false)
        viewModel.addTask(task)

        viewModel.toggleTaskCompletion(task.id)
        assertTrue(viewModel.tasks.value.first().isCompleted)
    }

    @Test
    fun viewModel_deleteTask_removesTask() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.tasks.collect {} }

        val task = Task(title = "To Delete")
        viewModel.addTask(task)

        viewModel.deleteTask(task.id)
        assertTrue(viewModel.tasks.value.isEmpty())
    }

    @Test
    fun viewModel_clearCompletedTasks_removesCompletedOnly() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.tasks.collect {} }

        val task1 = Task(title = "Active", isCompleted = false)
        val task2 = Task(title = "Completed", isCompleted = true)
        viewModel.addTask(task1)
        viewModel.addTask(task2)

        viewModel.clearCompletedTasks()
        assertEquals(1, viewModel.tasks.value.size)
        assertEquals("Active", viewModel.tasks.value.first().title)
    }

    @Test
    fun viewModel_filterTasks_filtersCorrectly() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.tasks.collect {} }

        val activeTask = Task(title = "Active", isCompleted = false)
        val completedTask = Task(title = "Completed", isCompleted = true)
        viewModel.addTask(activeTask)
        viewModel.addTask(completedTask)

        // ALL
        viewModel.setTaskFilter(TaskFilter.ALL)
        assertEquals(2, viewModel.tasks.value.size)

        // ACTIVE
        viewModel.setTaskFilter(TaskFilter.ACTIVE)
        assertEquals(1, viewModel.tasks.value.size)
        assertEquals(activeTask.id, viewModel.tasks.value.first().id)

        // COMPLETED
        viewModel.setTaskFilter(TaskFilter.COMPLETED)
        assertEquals(1, viewModel.tasks.value.size)
        assertEquals(completedTask.id, viewModel.tasks.value.first().id)
    }

    @Test
    fun viewModel_allTasks_containsAllTasksRegardlessOfFilter() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.allTasks.collect {} }

        val activeTask = Task(title = "Active", isCompleted = false)
        val completedTask = Task(title = "Completed", isCompleted = true)
        viewModel.addTask(activeTask)
        viewModel.addTask(completedTask)

        viewModel.setTaskFilter(TaskFilter.COMPLETED)
        assertEquals(2, viewModel.allTasks.value.size)
    }
}
