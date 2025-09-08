package com.example.codechallenge.features.user.presentation

data class SignUpFormState(
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val email: String = "",
    val emailError: UiError? = null,
    val name: String = "",
    val nameError: UiError? = null,
    val lastname: String = "",
    val lastnameError: UiError? = null,
    val password: String = "",
    val passwordError: UiError? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: UiError? = null,
) {
    val canSubmit: Boolean
        get() = !isSubmitting &&
                nameError == null && lastnameError == null &&
                passwordError == null &&
                confirmPasswordError == null
}
