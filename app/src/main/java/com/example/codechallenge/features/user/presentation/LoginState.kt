package com.example.codechallenge.features.user.presentation

data class LoginState(
    private val isLoading: Boolean = false,
    private val success: Boolean? = null,
    private val errorMessage: String? = null
)
