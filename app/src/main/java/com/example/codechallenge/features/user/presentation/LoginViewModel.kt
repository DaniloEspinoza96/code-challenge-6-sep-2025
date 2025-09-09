package com.example.codechallenge.features.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codechallenge.BuildConfig
import com.example.codechallenge.features.user.domain.repository.UserRepository
import com.example.codechallenge.features.user.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.codechallenge.utils.validateEmailWithUiError as utilsValidateEmail
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LoginFormState())
    val state: StateFlow<LoginFormState> get() = _state

    private val _effects = Channel<LoginEffect>(Channel.BUFFERED)
    val effects: Flow<LoginEffect> = _effects.receiveAsFlow()

    val userInfo = userRepository.loggedUser.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun onEmailChange(value: String) {
        _state.update { it.copy(email = value, emailError = validateEmail(value)) }
    }

    fun onPasswordChange(value: String) {
        _state.update { it.copy(password = value, passwordError = validatePassword(value)) }
    }

    fun onGoToSignup() {
        viewModelScope.launch { _effects.send(LoginEffect.GoToSignUp) }
    }

    fun onSubmit() {
        val state = _state.value
        val emailError = validateEmail(state.email)
        val passwordError = validatePassword(state.password)

        if (emailError != null || passwordError != null) {
            _state.update { it.copy(emailError = emailError, passwordError = passwordError) }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true) }
            loginUseCase(email = state.email, password = state.password).fold(
                onSuccess = {
                    _state.update { it.copy(isSubmitting = false) }
                    _effects.send(LoginEffect.LoggedIn)
                },
                onFailure = { e ->
                    _state.update { state -> state.copy(isSubmitting = false) }
                    _effects.send(LoginEffect.Error(e.message ?: "Hubo un error al iniciar sesión"))
                }
            )
        }
    }

    private fun validateEmail(email: String): UiError? {
        return utilsValidateEmail(email)
    }

    private fun validatePassword(password: String): UiError? {
        if (password.isBlank()) return UiError.REQUIRED
        if (password.length < BuildConfig.MINIMUM_PASSWORD_LENGTH) return UiError.INVALID
        return null
    }
}