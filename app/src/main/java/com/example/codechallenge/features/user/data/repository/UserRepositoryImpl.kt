package com.example.codechallenge.features.user.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.codechallenge.features.user.data.local.db.UserDao
import com.example.codechallenge.features.user.data.local.db.UserEntity
import com.example.codechallenge.features.user.domain.model.User
import com.example.codechallenge.features.user.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao
) : UserRepository {
    override suspend fun login(user: String, password: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun register(
        user: User
    ): Result<Unit> {
        return try {
            val userEntity = UserEntity.fromDomain(user)
            userDao.insertUser(userEntity)
            Result.success(Unit)
        } catch (e: SQLiteConstraintException) {
            Result.failure(Exception("El email ya está registrado"))
        }
    }
}