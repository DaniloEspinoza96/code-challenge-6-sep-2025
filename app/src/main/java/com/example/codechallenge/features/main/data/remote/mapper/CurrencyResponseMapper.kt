package com.example.codechallenge.features.main.data.remote.mapper

import com.example.codechallenge.features.main.data.remote.model.CurrencyResponse
import com.example.codechallenge.features.main.domain.model.CurrencyHistory

class CurrencyResponseMapper {
    companion object {
        fun fromResponseToDomain(currencyResponse: CurrencyResponse): CurrencyHistory {
            return CurrencyHistory(
                currency = currencyResponse.currency,
                entries = currencyResponse.entries.map { entry -> entry.toDomain() }
            )
        }
    }
}