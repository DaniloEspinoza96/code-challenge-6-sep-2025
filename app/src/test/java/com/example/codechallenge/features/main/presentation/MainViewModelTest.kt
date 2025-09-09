package com.example.codechallenge.features.main.presentation

import app.cash.turbine.test
import com.example.codechallenge.MainDispatcherRule
import com.example.codechallenge.features.main.domain.model.Currency
import com.example.codechallenge.features.main.domain.model.CurrencyEntry
import com.example.codechallenge.features.main.domain.model.CurrencyHistory
import com.example.codechallenge.features.main.domain.usecase.GetCurrencyHistoryUseCase
import com.example.codechallenge.features.user.data.repository.FakeUserRepository
import com.example.codechallenge.features.user.domain.repository.UserRepository
import com.example.codechallenge.features.user.domain.usecase.LogoutUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var userRepository: UserRepository
    private lateinit var getCurrencyHistoryUseCase: GetCurrencyHistoryUseCase
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        userRepository = mockk(relaxed = true)
        getCurrencyHistoryUseCase = mockk()
        viewModel = MainViewModel(getCurrencyHistoryUseCase, mockk(), userRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `get currency history and update state`() =
        runTest {
            val dollarCurrencyHistoryMock = CurrencyHistory(
                currency = "Dolar",
                entries = listOf(CurrencyEntry(value = 1000.15, "2025-09-08"))
            )
            val euroCurrencyHistoryMock = CurrencyHistory(
                currency = "Euro",
                entries = listOf(CurrencyEntry(value = 1000.15, "2025-09-08"))
            )
            coEvery {
                getCurrencyHistoryUseCase.invoke(
                    Currency.DOLLAR,
                    any()
                )
            } returns Result.success(dollarCurrencyHistoryMock)
            coEvery {
                getCurrencyHistoryUseCase.invoke(
                    Currency.EURO,
                    any()
                )
            } returns Result.success(euroCurrencyHistoryMock)
            every {
                userRepository.loggedUser
            } returns MutableStateFlow(null)

            viewModel.state.test {
                viewModel.onStart()
                advanceUntilIdle()

                skipItems(1)
                val state = awaitItem()
                assertNotNull(state.dollarHistory)
                assertNotNull(state.euroHistory)
                assertTrue(!state.isLoading)

                cancelAndIgnoreRemainingEvents()
            }

        }

    @Test
    fun `fails to get currency history and update state with failure`() =
        runTest {
            val errorMsg = "Hubo un problema consiguiendo los datos"
            coEvery {
                getCurrencyHistoryUseCase.invoke(
                    Currency.DOLLAR,
                    any()
                )
            } returns Result.failure(Exception(errorMsg))
            coEvery {
                getCurrencyHistoryUseCase.invoke(
                    Currency.EURO,
                    any()
                )
            } returns Result.failure(Exception(errorMsg))
            every {
                userRepository.loggedUser
            } returns MutableStateFlow(null)

            viewModel.state.test {
                viewModel.onStart()
                advanceUntilIdle()

                skipItems(1)
                val state = awaitItem()
                assertNull(state.dollarHistory)
                assertNull(state.euroHistory)
                assertTrue(!state.isLoading)
                assert(errorMsg == state.errorMessage)

                cancelAndIgnoreRemainingEvents()
            }

        }

    @Test
    fun `when user logs out, it should be cleared from memory`() = runTest {
        val fakeRepo: UserRepository = FakeUserRepository()

        val logoutUseCase = LogoutUseCase(fakeRepo)
        val mainViewModel = MainViewModel(mockk(), logoutUseCase, fakeRepo)

        mainViewModel.userInfo.test {
            val initial = awaitItem()

            mainViewModel.onLogout()

            assertNotNull(initial)
            val afterLogout = awaitItem()
            assertNull(afterLogout)
            cancelAndIgnoreRemainingEvents()
        }
    }
}