package com.example.codechallenge.features.user.presentation

interface SignUpEffect {
    data object SignedUp: SignUpEffect
    data object OnGoToLogin: SignUpEffect
    data class Error(val message: String): SignUpEffect
}