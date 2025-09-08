package com.example.codechallenge.features.main.domain.usecase

import com.example.codechallenge.BuildConfig
import com.example.codechallenge.features.main.domain.model.Currency
import com.example.codechallenge.features.main.domain.model.CurrencyHistory
import com.example.codechallenge.features.main.domain.repository.CurrencyRepository
import javax.inject.Inject

class GetCurrencyHistoryUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository
) {
    suspend operator fun invoke(currency: Currency, dateInMillis: Long): Result<CurrencyHistory> =
        currencyRepository.getCurrencyHistory(
            currency,
            monthsRange = BuildConfig.MONTHS_RANGE.toLong(),
            dateInMillis = dateInMillis
        )
}