package com.example.codechallenge.features.user.data.repository

import com.example.codechallenge.features.user.domain.model.User
import com.example.codechallenge.features.user.domain.model.UserInfo
import com.example.codechallenge.features.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeUserRepository(initialUser: UserInfo? = UserInfo("Juan", "Perez", "test@correo.com")) : UserRepository {
    private val _loggedUser = MutableStateFlow(initialUser)
    override val loggedUser: Flow<UserInfo?> = _loggedUser
    override suspend fun login(email: String, password: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        _loggedUser.value = null
    }

    override suspend fun register(user: User): Result<Unit> {
        TODO("Not yet implemented")
    }
}