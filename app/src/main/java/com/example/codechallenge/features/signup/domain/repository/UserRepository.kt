package com.example.codechallenge.features.signup.domain.repository

interface UserRepository {
    suspend fun signUp(email: String, name: String, lastName: String, password: String): Result<Unit>
}