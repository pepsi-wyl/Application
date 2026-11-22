package site.ylan.application.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class AuthDestination {
    WELCOME,
    LOGIN,
    SIGN_UP,
    TASK_MANAGER
}

data class AuthUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val authError: String? = null,
    val isLoading: Boolean = false,
    val currentDestination: AuthDestination = AuthDestination.WELCOME,
    val isLoggedIn: Boolean = false
)

class AuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name, authError = null) }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email, authError = null) }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password, authError = null) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, authError = null) }
    }

    fun navigateTo(destination: AuthDestination) {
        _uiState.update {
            it.copy(
                currentDestination = destination,
                authError = null
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(authError = null) }
    }

    fun login(onSuccess: () -> Unit = {}) {
        val currentState = _uiState.value
        when {
            currentState.email.isBlank() -> {
                _uiState.update { it.copy(authError = "Please enter an email or username") }
            }
            currentState.password.isBlank() -> {
                _uiState.update { it.copy(authError = "Please enter your password") }
            }
            else -> {
                _uiState.update {
                    it.copy(
                        isLoggedIn = true,
                        authError = null,
                        currentDestination = AuthDestination.TASK_MANAGER
                    )
                }
                onSuccess()
            }
        }
    }

    fun signUp(onSuccess: () -> Unit = {}) {
        val currentState = _uiState.value
        when {
            currentState.name.isBlank() -> {
                _uiState.update { it.copy(authError = "Please enter your name") }
            }
            currentState.email.isBlank() -> {
                _uiState.update { it.copy(authError = "Please enter your email") }
            }
            currentState.password.isBlank() -> {
                _uiState.update { it.copy(authError = "Please enter a password") }
            }
            currentState.password.length < 6 -> {
                _uiState.update { it.copy(authError = "Password must be at least 6 characters") }
            }
            currentState.password != currentState.confirmPassword -> {
                _uiState.update { it.copy(authError = "Passwords do not match") }
            }
            else -> {
                _uiState.update {
                    it.copy(
                        isLoggedIn = true,
                        authError = null,
                        currentDestination = AuthDestination.TASK_MANAGER
                    )
                }
                onSuccess()
            }
        }
    }

    fun googleSignIn(onSuccess: () -> Unit = {}) {
        _uiState.update {
            it.copy(
                isLoggedIn = true,
                authError = null,
                currentDestination = AuthDestination.TASK_MANAGER
            )
        }
        onSuccess()
    }

    fun logout() {
        _uiState.update {
            AuthUiState(currentDestination = AuthDestination.WELCOME)
        }
    }

    fun resetForm() {
        _uiState.update {
            it.copy(
                name = "",
                email = "",
                password = "",
                confirmPassword = "",
                authError = null
            )
        }
    }
}
