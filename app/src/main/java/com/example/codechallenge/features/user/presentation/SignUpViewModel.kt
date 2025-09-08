package com.example.codechallenge.features.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codechallenge.BuildConfig
import com.example.codechallenge.features.user.domain.model.User
import com.example.codechallenge.features.user.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.codechallenge.utils.validateEmail as utilsValidateEmail
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<SignUpFormState> = MutableStateFlow(SignUpFormState())
    val state: StateFlow<SignUpFormState> get() = _state

    private val _effects = Channel<SignUpEffect>(Channel.BUFFERED)
    val effects: Flow<SignUpEffect> = _effects.receiveAsFlow()

    fun onNameChange(value: String) {
        _state.update { it.copy(name = value, nameError = validateName(value)) }

    }

    fun onLastNameChange(value: String) {
        _state.update { it.copy(lastname = value, lastnameError = validateLastname(value)) }

    }

    fun onEmailChange(value: String) {
        _state.update { it.copy(email = value, emailError = validateEmail(value)) }
    }

    fun onPasswordChange(value: String) {
        _state.update { it.copy(password = value, passwordError = validatePassword(value)) }
    }

    fun onConfirmPasswordChange(value: String) {
        _state.update {
            it.copy(
                confirmPassword = value,
                confirmPasswordError = validateConfirmPassword(
                    password = it.password,
                    confirmPassword = value
                )
            )
        }
    }

    fun onGoToLogin() {
        viewModelScope.launch { _effects.send(SignUpEffect.OnGoToLogin) }
    }

    fun onSubmit() {
        val state = _state.value
        val emailError = validateEmail(state.email)
        val nameError = validateName(state.name)
        val lastNameError = validateLastname(state.lastname)
        val passwordError = validatePassword(state.password)
        val confirmPasswordError = validateConfirmPassword(state.password, state.confirmPassword)

        if (emailError != null || passwordError != null || nameError != null || lastNameError != null || confirmPasswordError != null) {
            _state.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError,
                    nameError = nameError,
                    lastnameError = lastNameError,
                    confirmPasswordError = confirmPasswordError
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSubmitting = true) }
            signUpUseCase(
                User(
                    name = state.name,
                    lastname = state.lastname,
                    email = state.email,
                    password = state.password
                )
            ).fold(
                onSuccess = {
                    _state.update { it.copy(isSubmitting = false) }
                    _effects.send(SignUpEffect.SignedUp)
                },
                onFailure = {
                    _state.update { state ->  state.copy(isSubmitting = false) }
                    _effects.send(SignUpEffect.Error(it.message ?: "Hubo un error al registrarse"))
                }
            )
        }
    }

    private fun validateName(name: String): UiError? {
        if (name.isBlank()) return UiError.REQUIRED
        return if (name.length < 2) UiError.INVALID else null
    }

    private fun validateLastname(lastName: String): UiError? {
        if (lastName.isBlank()) return UiError.REQUIRED
        return if (lastName.length < 2) UiError.INVALID else null
    }

    private fun validateEmail(email: String): UiError? {
        return utilsValidateEmail(email)
    }

    private fun validatePassword(password: String): UiError? {
        if (password.isBlank()) return UiError.REQUIRED
        return if (password.length < BuildConfig.MINIMUM_PASSWORD_LENGTH) UiError.INVALID else null
    }

    private fun validateConfirmPassword(password: String, confirmPassword: String): UiError? {
        return if (password != confirmPassword) UiError.INVALID else null
    }
}