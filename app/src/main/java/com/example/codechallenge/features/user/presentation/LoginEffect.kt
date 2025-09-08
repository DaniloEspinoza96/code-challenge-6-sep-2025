package com.example.codechallenge.features.user.presentation

interface LoginEffect {
    data object LoggedIn: LoginEffect
    data class Error(val message: String): LoginEffect
    data object GoToSignUp: LoginEffect
}