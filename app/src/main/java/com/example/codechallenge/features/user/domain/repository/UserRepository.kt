package com.example.codechallenge.features.user.domain.repository

import com.example.codechallenge.features.user.domain.model.User

interface UserRepository {
    suspend fun login(user: String, password: String): Result<Unit>
    suspend fun logout(): Result<Unit>
    suspend fun register(user: User): Result<Unit>
}