package site.ylan.application.viewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: AuthViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialUiState_isWelcomeAndLoggedOut() {
        val state = viewModel.uiState.value
        assertEquals(AuthDestination.WELCOME, state.currentDestination)
        assertFalse(state.isLoggedIn)
        assertNull(state.authError)
    }

    @Test
    fun onNameChanged_updatesStateAndClearsError() {
        viewModel.login() // Sets error
        viewModel.onNameChanged("John Doe")

        val state = viewModel.uiState.value
        assertEquals("John Doe", state.name)
        assertNull(state.authError)
    }

    @Test
    fun onEmailChanged_updatesStateAndClearsError() {
        viewModel.login() // Sets error
        viewModel.onEmailChanged("john@example.com")

        val state = viewModel.uiState.value
        assertEquals("john@example.com", state.email)
        assertNull(state.authError)
    }

    @Test
    fun onPasswordChanged_updatesState() {
        viewModel.onPasswordChanged("secret123")
        assertEquals("secret123", viewModel.uiState.value.password)
    }

    @Test
    fun onConfirmPasswordChanged_updatesState() {
        viewModel.onConfirmPasswordChanged("secret123")
        assertEquals("secret123", viewModel.uiState.value.confirmPassword)
    }

    @Test
    fun navigateTo_changesDestinationAndClearsError() {
        viewModel.login()
        viewModel.navigateTo(AuthDestination.SIGN_UP)

        val state = viewModel.uiState.value
        assertEquals(AuthDestination.SIGN_UP, state.currentDestination)
        assertNull(state.authError)
    }

    @Test
    fun login_withBlankEmail_setsError() {
        viewModel.onPasswordChanged("secret")
        viewModel.login()

        val state = viewModel.uiState.value
        assertFalse(state.isLoggedIn)
        assertEquals("Please enter an email or username", state.authError)
    }

    @Test
    fun login_withBlankPassword_setsError() {
        viewModel.onEmailChanged("user@example.com")
        viewModel.login()

        val state = viewModel.uiState.value
        assertFalse(state.isLoggedIn)
        assertEquals("Please enter your password", state.authError)
    }

    @Test
    fun login_withValidCredentials_succeeds() {
        var successTriggered = false
        viewModel.onEmailChanged("user@example.com")
        viewModel.onPasswordChanged("password123")
        viewModel.login { successTriggered = true }

        val state = viewModel.uiState.value
        assertTrue(state.isLoggedIn)
        assertNull(state.authError)
        assertEquals(AuthDestination.TASK_MANAGER, state.currentDestination)
        assertTrue(successTriggered)
    }

    @Test
    fun signUp_withBlankName_setsError() {
        viewModel.signUp()
        assertEquals("Please enter your name", viewModel.uiState.value.authError)
    }

    @Test
    fun signUp_withBlankEmail_setsError() {
        viewModel.onNameChanged("Alice")
        viewModel.signUp()
        assertEquals("Please enter your email", viewModel.uiState.value.authError)
    }

    @Test
    fun signUp_withBlankPassword_setsError() {
        viewModel.onNameChanged("Alice")
        viewModel.onEmailChanged("alice@example.com")
        viewModel.signUp()
        assertEquals("Please enter a password", viewModel.uiState.value.authError)
    }

    @Test
    fun signUp_withShortPassword_setsError() {
        viewModel.onNameChanged("Alice")
        viewModel.onEmailChanged("alice@example.com")
        viewModel.onPasswordChanged("12345")
        viewModel.onConfirmPasswordChanged("12345")
        viewModel.signUp()
        assertEquals("Password must be at least 6 characters", viewModel.uiState.value.authError)
    }

    @Test
    fun signUp_withPasswordMismatch_setsError() {
        viewModel.onNameChanged("Alice")
        viewModel.onEmailChanged("alice@example.com")
        viewModel.onPasswordChanged("password123")
        viewModel.onConfirmPasswordChanged("password456")
        viewModel.signUp()
        assertEquals("Passwords do not match", viewModel.uiState.value.authError)
    }

    @Test
    fun signUp_withValidInputs_succeeds() {
        var successTriggered = false
        viewModel.onNameChanged("Alice")
        viewModel.onEmailChanged("alice@example.com")
        viewModel.onPasswordChanged("password123")
        viewModel.onConfirmPasswordChanged("password123")
        viewModel.signUp { successTriggered = true }

        val state = viewModel.uiState.value
        assertTrue(state.isLoggedIn)
        assertNull(state.authError)
        assertEquals(AuthDestination.TASK_MANAGER, state.currentDestination)
        assertTrue(successTriggered)
    }

    @Test
    fun googleSignIn_succeeds() {
        var successTriggered = false
        viewModel.googleSignIn { successTriggered = true }

        val state = viewModel.uiState.value
        assertTrue(state.isLoggedIn)
        assertNull(state.authError)
        assertEquals(AuthDestination.TASK_MANAGER, state.currentDestination)
        assertTrue(successTriggered)
    }

    @Test
    fun logout_resetsState() {
        viewModel.onEmailChanged("test@example.com")
        viewModel.googleSignIn()
        viewModel.logout()

        val state = viewModel.uiState.value
        assertFalse(state.isLoggedIn)
        assertEquals(AuthDestination.WELCOME, state.currentDestination)
        assertEquals("", state.email)
    }

    @Test
    fun resetForm_clearsInputFields() {
        viewModel.onNameChanged("Name")
        viewModel.onEmailChanged("Email")
        viewModel.onPasswordChanged("Pass")
        viewModel.onConfirmPasswordChanged("Pass")
        viewModel.resetForm()

        val state = viewModel.uiState.value
        assertEquals("", state.name)
        assertEquals("", state.email)
        assertEquals("", state.password)
        assertEquals("", state.confirmPassword)
    }
}
