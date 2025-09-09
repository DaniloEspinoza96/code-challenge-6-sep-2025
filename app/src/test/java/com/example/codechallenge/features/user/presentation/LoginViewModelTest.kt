package com.example.codechallenge.features.user.presentation

import app.cash.turbine.test
import com.example.codechallenge.MainDispatcherRule
import com.example.codechallenge.features.user.domain.repository.UserRepository
import com.example.codechallenge.features.user.domain.usecase.LoginUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var userRepository: UserRepository
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        userRepository = mockk(relaxed = true)
        loginUseCase = mockk()
        viewModel = LoginViewModel(loginUseCase, userRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `onEmailChange updates email and validates`() = runTest {
        viewModel.onEmailChange("invalid-email")
        assertEquals("invalid-email", viewModel.state.value.email)
        assertNotNull(viewModel.state.value.emailError)
    }

    @Test
    fun `onPasswordChange updates password and validates`() = runTest {
        viewModel.onPasswordChange("12")// password too short
        assertEquals("12", viewModel.state.value.password)
        assertNotNull(viewModel.state.value.passwordError)
    }

    @Test
    fun `onGoToSignup emits GoToSignUp effect`() = runTest {
        viewModel.effects.test {
            viewModel.onGoToSignup()
            assertEquals(LoginEffect.GoToSignUp, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onSubmit success updates state and emits LoggedIn`() = runTest {
        // Arrange
        every { userRepository.loggedUser } returns MutableStateFlow(null)
        coEvery { loginUseCase("juan@perez.com", "123456") } returns Result.success(Unit)

        viewModel.onEmailChange("juan@perez.com")
        viewModel.onPasswordChange("123456")

        viewModel.effects.test {
            viewModel.onSubmit()
            val effect = awaitItem()
            assertEquals(LoginEffect.LoggedIn, effect)
            cancelAndIgnoreRemainingEvents()
        }
        assertFalse(viewModel.state.value.isSubmitting)
    }

    @Test
    fun `onSubmit failure updates state and emits Error`() = runTest {
        every { userRepository.loggedUser } returns MutableStateFlow(null)
        coEvery { loginUseCase(any(), any()) } returns Result.failure(Exception("Credenciales inválidas"))

        viewModel.onEmailChange("user@test.com")
        viewModel.onPasswordChange("badpwd")

        viewModel.effects.test {
            viewModel.onSubmit()
            val effect = awaitItem()

            assertTrue(effect is LoginEffect.Error)
            assertEquals("Credenciales inválidas", (effect as LoginEffect.Error).message)
            cancelAndIgnoreRemainingEvents()
        }
        assertFalse(viewModel.state.value.isSubmitting)
    }
}