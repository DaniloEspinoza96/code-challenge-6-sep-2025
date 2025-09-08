package com.example.codechallenge.features.main.domain.repository

import com.example.codechallenge.features.main.domain.model.Currency
import com.example.codechallenge.features.main.domain.model.CurrencyHistory

interface CurrencyRepository {
    suspend fun getCurrencyHistory(currency: Currency, monthsRange: Long, dateInMillis: Long): Result<CurrencyHistory>
}