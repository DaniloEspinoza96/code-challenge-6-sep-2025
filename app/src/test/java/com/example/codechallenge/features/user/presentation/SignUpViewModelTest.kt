package com.example.codechallenge.features.user.presentation

import app.cash.turbine.test
import com.example.codechallenge.MainDispatcherRule
import com.example.codechallenge.features.user.domain.model.User
import com.example.codechallenge.features.user.domain.usecase.SignUpUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import kotlin.test.Test

class SignUpViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var signUpUseCase: SignUpUseCase
    private lateinit var viewModel: SignUpViewModel

    @Before
    fun setUp() {
        signUpUseCase = mockk()
        viewModel = SignUpViewModel(signUpUseCase)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `onNameChange updates name and validates`() = runTest {
        viewModel.onNameChange("A") // muy corto
        assertEquals("A", viewModel.state.value.name)
        assertNotNull(viewModel.state.value.nameError)
    }

    @Test
    fun `onLastNameChange updates lastname and validates`() = runTest {
        viewModel.onLastNameChange("")
        assertEquals("", viewModel.state.value.lastname)
        assertNotNull(viewModel.state.value.lastnameError)
    }

    @Test
    fun `onGoToLogin emits effect`() = runTest {
        viewModel.effects.test {
            viewModel.onGoToLogin()
            assertEquals(SignUpEffect.OnGoToLogin, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSubmit success updates state and emits SignedUp`() = runTest {
        val user = User("Juan", "Perez", "user@test.com", "123456")
        coEvery { signUpUseCase(user) } returns Result.success(Unit)

        viewModel.onNameChange(user.name)
        viewModel.onLastNameChange(user.lastname)
        viewModel.onEmailChange(user.email)
        viewModel.onPasswordChange(user.password)
        viewModel.onConfirmPasswordChange(user.password)

        viewModel.effects.test {
            viewModel.onSubmit()
            val effect = awaitItem()
            assertEquals(SignUpEffect.SignedUp, effect)
            assertTrue(!viewModel.state.value.isSubmitting)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSubmit failure updates state and emits Error`() = runTest {
        val user = User("Juan", "Perez", "user@test.com", "123456")
        coEvery { signUpUseCase(user) } returns Result.failure(Exception("El email ya está registrado"))

        viewModel.onNameChange(user.name)
        viewModel.onLastNameChange(user.lastname)
        viewModel.onEmailChange(user.email)
        viewModel.onPasswordChange(user.password)
        viewModel.onConfirmPasswordChange(user.password)

        viewModel.effects.test {
            viewModel.onSubmit()
            val effect = awaitItem()
            assertTrue(effect is SignUpEffect.Error)
            assertEquals("El email ya está registrado", (effect as SignUpEffect.Error).message)
            assertTrue(!viewModel.state.value.isSubmitting)
            cancelAndIgnoreRemainingEvents()
        }
    }
}