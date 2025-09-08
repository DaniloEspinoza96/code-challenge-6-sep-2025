package com.example.codechallenge.features.main.data.repository

import com.example.codechallenge.BuildConfig
import com.example.codechallenge.features.main.data.remote.mapper.CurrencyResponseMapper
import com.example.codechallenge.features.main.data.remote.service.CurrencyService
import com.example.codechallenge.features.main.domain.model.Currency
import com.example.codechallenge.features.main.domain.model.CurrencyHistory
import com.example.codechallenge.features.main.domain.repository.CurrencyRepository
import com.example.codechallenge.utils.minusMonths
import java.time.ZoneId
import java.util.Date
import javax.inject.Inject

class CurrencyRepositoryImpl @Inject constructor(
    private val currencyService: CurrencyService
) : CurrencyRepository {
    override suspend fun getCurrencyHistory(
        currency: Currency,
        monthsRange: Long,
        dateInMillis: Long
    ): Result<CurrencyHistory> = runCatching {
        val formattedCurrency = formatCurrency(currency)

        val startDate = Date(dateInMillis).minusMonths(monthsRange)
        val endDate = Date(dateInMillis)

        val formattedStartDate = formatDate(startDate, "dias_i")
        val formattedEndDate = formatDate(endDate, "dias_f")

        val response = currencyService.getCurrencyHistory(
            currency = formattedCurrency,
            startDate = formattedStartDate,
            endDate = formattedEndDate
        )

        if (response.isSuccessful && response.body() != null) {
            CurrencyResponseMapper.fromResponseToDomain(response.body()!!)
        } else {
            throw Exception("Ocurrió un problema obteniendo el historial del ${if (currency == Currency.DOLLAR) "dólar" else "euro"}")
        }
    }

    private fun formatDate(date: Date, format: String): String {
        val month = date.toInstant().atZone(ZoneId.of(BuildConfig.TIMEZONE)).monthValue.toString()
            .padStart(2, '0')
        val day = date.toInstant().atZone(ZoneId.of(BuildConfig.TIMEZONE)).dayOfMonth.toString()
            .padStart(2, '0')
        val year = date.toInstant().atZone(ZoneId.of(BuildConfig.TIMEZONE)).year

        return "${year}/${month}/${format}/${day}"
    }

    private fun formatCurrency(currency: Currency): String = when (currency) {
        Currency.DOLLAR -> "dolar"
        Currency.EURO -> "euro"
    }

}