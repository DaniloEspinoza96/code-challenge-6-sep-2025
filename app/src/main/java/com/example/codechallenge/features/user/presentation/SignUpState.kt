package com.example.codechallenge.features.user.presentation

data class SignUpState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean? = null
)
