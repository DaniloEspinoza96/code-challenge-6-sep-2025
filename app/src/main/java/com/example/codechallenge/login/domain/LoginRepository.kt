package com.example.codechallenge.login.domain

interface LoginRepository {
    suspend fun login(user: String, password: String): Result<Boolean>
    suspend fun logout(): Result<Unit>
    suspend fun signUp(email: String, name: String, lastName: String, password: String): Result<Unit>
}