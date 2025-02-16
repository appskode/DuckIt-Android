package com.hassan.duckit.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hassan.duckit.domain.repository.AuthRepository
import com.hassan.duckit.util.Validation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthScreenState())
    val uiState = _uiState.asStateFlow()

    private val _authScreenEvent = MutableStateFlow<AuthScreenEvent>(AuthScreenEvent.None)
    val authScreenEvent = _authScreenEvent.asStateFlow()

    init {
        checkAuthStatus()
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            val token = authRepository.getStoredToken()
            _uiState.update {
                it.copy(isAuthenticated = token != null)
            }
        }
    }

    fun onEvent(action: AuthScreenAction) {
        when (action) {
            is AuthScreenAction.SignIn -> signIn(action.email, action.password)
            is AuthScreenAction.SignUp -> signUp(action.email, action.password)
            is AuthScreenAction.ValidateEmail -> validateEmail(action.email)
            is AuthScreenAction.ValidatePassword -> validatePassword(action.password)
            AuthScreenAction.ToggleMode -> toggleMode()
            AuthScreenAction.Logout -> logout()
        }
    }

    private fun logout() {
        viewModelScope.launch {
            authRepository.clearToken()
            _uiState.update {
                it.copy(
                    isAuthenticated = false,
                    isSignInMode = true,
                    error = null
                )
            }
            _authScreenEvent.value = AuthScreenEvent.LogoutSuccess
        }
    }

    private fun validateEmail(email: String) {
        when (val result = Validation.validateEmail(email)) {
            is Validation.ValidationResult.Error -> {
                _uiState.update { it.copy(emailError = result.message) }
            }
            Validation.ValidationResult.Success -> {
                _uiState.update { it.copy(emailError = null) }
            }
        }
    }

    private fun validatePassword(password: String) {
        when (val result = Validation.validatePassword(password)) {
            is Validation.ValidationResult.Error -> {
                _uiState.update { it.copy(passwordError = result.message) }
            }
            Validation.ValidationResult.Success -> {
                _uiState.update { it.copy(passwordError = null) }
            }
        }
    }

    private fun validateInputs(email: String, password: String): Boolean {
        val emailResult = Validation.validateEmail(email)
        val passwordResult = Validation.validatePassword(password)

        _uiState.update { state ->
            state.copy(
                emailError = (emailResult as? Validation.ValidationResult.Error)?.message,
                passwordError = (passwordResult as? Validation.ValidationResult.Error)?.message
            )
        }

        return emailResult is Validation.ValidationResult.Success &&
                passwordResult is Validation.ValidationResult.Success
    }

    private fun signIn(email: String, password: String) {
        if (!validateInputs(email, password)) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            authRepository.signIn(email, password)
                .onSuccess {
                    _uiState.update { it.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )}
                    _authScreenEvent.value = AuthScreenEvent.LoginSuccess
                }
                .onFailure { error ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = error.message
                    )}
                }
        }
    }

    private fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            authRepository.signUp(email, password)
                .onSuccess {
                    _uiState.update { it.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )}
                    _authScreenEvent.value = AuthScreenEvent.LoginSuccess
                }
                .onFailure { error ->
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = error.message
                    )}
                }
        }
    }

    private fun toggleMode() {
        _uiState.update { it.copy(
            isSignInMode = !it.isSignInMode,
            error = null
        )}
    }
}

data class AuthScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSignInMode: Boolean = true,
    val isAuthenticated: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
)

sealed interface AuthScreenEvent {
    data object LoginSuccess : AuthScreenEvent
    data object LogoutSuccess : AuthScreenEvent
    data object None : AuthScreenEvent
}

sealed interface AuthScreenAction {
    data class SignIn(val email: String, val password: String) : AuthScreenAction
    data class SignUp(val email: String, val password: String) : AuthScreenAction
    data class ValidateEmail(val email: String) : AuthScreenAction
    data class ValidatePassword(val password: String) : AuthScreenAction
    data object ToggleMode : AuthScreenAction
    data object Logout : AuthScreenAction
}
