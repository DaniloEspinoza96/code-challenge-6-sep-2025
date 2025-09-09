package com.example.codechallenge.features.main.data.repository

import com.example.codechallenge.features.main.data.remote.model.CurrencyResponse
import com.example.codechallenge.features.main.data.remote.service.CurrencyService
import com.example.codechallenge.features.main.domain.model.Currency
import com.example.codechallenge.features.main.domain.model.CurrencyHistory
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.util.*

class CurrencyRepositoryImplTest {

    private lateinit var currencyService: CurrencyService
    private lateinit var repository: CurrencyRepositoryImpl

    @Before
    fun setUp() {
        currencyService = mockk()
        repository = CurrencyRepositoryImpl(currencyService)
    }

    @Test
    fun `getCurrencyHistory returns success when response is valid`() = runTest {
        val fakeResponse = mockk<CurrencyResponse>()
        val expectedHistory = CurrencyHistory("dolar", emptyList())

        coEvery {
            currencyService.getCurrencyHistory(any(), any(), any())
        } returns Response.success(fakeResponse)

        io.mockk.mockkObject(com.example.codechallenge.features.main.data.remote.mapper.CurrencyResponseMapper)
        io.mockk.every {
            com.example.codechallenge.features.main.data.remote.mapper.CurrencyResponseMapper.fromResponseToDomain(fakeResponse)
        } returns expectedHistory

        val result = repository.getCurrencyHistory(
            currency = Currency.DOLLAR,
            monthsRange = 6,
            dateInMillis = Date().time
        )

        assertTrue(result.isSuccess)
        assertEquals("dolar", result.getOrNull()?.currency)
    }

    @Test
    fun `getCurrencyHistory returns failure when response is error`() = runTest {
        coEvery {
            currencyService.getCurrencyHistory(any(), any(), any())
        } returns Response.error(404, ResponseBody.create(null, ""))

        val result = repository.getCurrencyHistory(
            currency = Currency.EURO,
            monthsRange = 3,
            dateInMillis = Date().time
        )

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()!!.message!!.contains("euro"))
    }
}