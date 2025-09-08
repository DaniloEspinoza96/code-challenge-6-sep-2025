package com.example.codechallenge.features.main.data.remote.model

import com.example.codechallenge.features.main.domain.model.CurrencyEntry as DomainCurrencyEntry
import com.squareup.moshi.Json

data class CurrencyEntry(
    @Json(name = "Valor") val value: Double,
    @Json(name = "Fecha") val date: String
) {
    fun toDomain(): DomainCurrencyEntry {
        return DomainCurrencyEntry(
            value = this.value,
            date = this.date
        )
    }
}
