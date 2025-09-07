package com.example.codechallenge.features.user.domain.repository

import com.example.codechallenge.features.user.domain.model.User
import com.example.codechallenge.features.user.domain.model.UserInfo
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun logout()
    suspend fun register(user: User): Result<Unit>
    val loggedUser: Flow<UserInfo?>
}