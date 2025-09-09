package com.example.codechallenge.features.user.data.repository

import android.database.sqlite.SQLiteConstraintException
import com.example.codechallenge.features.user.data.local.datastore.UserPreferences
import com.example.codechallenge.features.user.data.local.db.UserDao
import com.example.codechallenge.features.user.data.local.db.UserEntity
import com.example.codechallenge.features.user.domain.model.User
import com.example.codechallenge.features.user.domain.model.UserInfo
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.*

class UserRepositoryImplTest {

    private lateinit var userDao: UserDao
    private lateinit var userPreferences: UserPreferences
    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setUp() {
        userDao = mockk()
        userPreferences = mockk(relaxed = true)
        repository = UserRepositoryImpl(userDao, userPreferences)
    }

    @Test
    fun `login succeeds when email and password match`() = runTest {
        val entity = UserEntity(id = 1, name = "Juan", lastname = "Perez", email = "test@test.com", password = "123456")
        coEvery { userDao.getUserByEmail("test@test.com") } returns entity
        coEvery { userPreferences.saveUser(any()) } just Runs

        val result = repository.login("test@test.com", "123456")

        assertTrue(result.isSuccess)
        coVerify { userPreferences.saveUser(match { it.email == "test@test.com" }) }
    }

    @Test
    fun `login fails when password does not match`() = runTest {
        val entity = UserEntity(id = 1, name = "Juan", lastname = "Perez", email = "test@test.com", password = "123456")
        coEvery { userDao.getUserByEmail("test@test.com") } returns entity

        val result = repository.login("test@test.com", "wrong")

        assertTrue(result.isFailure)
        coVerify(exactly = 0) { userPreferences.saveUser(any()) }
    }

    @Test
    fun `register fails when email already exists`() = runTest {
        val user = User("Juan", "Perez", "dup@test.com", "123456")
        coEvery { userDao.insertUser(any()) } throws SQLiteConstraintException()

        val result = repository.register(user)

        assertTrue(result.isFailure)
        assertEquals("El email ya está registrado", result.exceptionOrNull()?.message)
    }

    @Test
    fun `logout clears preferences`() = runTest {
        coEvery { userPreferences.clearUser() } just Runs

        repository.logout()

        coVerify { userPreferences.clearUser() }
    }

    @Test
    fun `loggedUser is delegated to userPreferences`() {
        val flow = MutableStateFlow<UserInfo?>(null)
        every { userPreferences.userFlow } returns flow

        val repo = UserRepositoryImpl(userDao, userPreferences)

        assertSame(flow, repo.loggedUser)
    }
}