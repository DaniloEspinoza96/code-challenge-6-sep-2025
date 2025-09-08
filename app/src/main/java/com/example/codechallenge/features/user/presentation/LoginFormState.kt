package com.example.codechallenge.features.user.presentation

data class LoginFormState(
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val emailError: UiError? = null,
    val passwordError: UiError? = null,
) {
    val canSubmit: Boolean get() = !isSubmitting && emailError == null && passwordError == null
}
