package com.example.codechallenge.features.user.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.codechallenge.features.user.data.local.datastore.UserPreferences
import com.example.codechallenge.features.user.data.local.db.UserDao
import com.example.codechallenge.features.user.data.local.db.UserEntity
import com.example.codechallenge.features.user.domain.model.User
import com.example.codechallenge.features.user.domain.model.UserInfo
import com.example.codechallenge.features.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/*
Users will be stored in a local db, for this challenge's purpose, even if we all know it would be
preferable to store it in a service. Passwords will be hashed here, even if we know the hash must be
done in the backend.
 */
class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val userPreferences: UserPreferences
) : UserRepository {
    override suspend fun login(email: String, password: String): Result<Unit> {
        val storedUser = userDao.getUserByEmail(email)
        storedUser?.let { userEntity ->
            if (userEntity.password == password) {
                storeLoggedUser(storedUser)
                return Result.success(Unit)
            }
        }
        return Result.failure(Exception("El usuario o la contraseña son incorrectos"))
    }

    override suspend fun logout() {
        userPreferences.clearUser()
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

    override val loggedUser: Flow<UserInfo?> = userPreferences.userFlow

    private suspend fun storeLoggedUser(userEntity: UserEntity) {
        val userInfo = UserInfo(
            name = userEntity.name,
            lastname = userEntity.lastname,
            email = userEntity.email
        )

        userPreferences.saveUser(userInfo)
    }
}