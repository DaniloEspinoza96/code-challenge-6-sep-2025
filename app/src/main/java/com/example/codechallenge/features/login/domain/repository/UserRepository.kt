package com.example.codechallenge.features.login.domain.repository

interface UserRepository {
    suspend fun login(user: String, password: String): Result<Boolean>
    suspend fun logout(): Result<Unit>
}