package com.example.codechallenge.features.main.data.remote.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyResponse(
    val currency: String,
    val entries: List<CurrencyEntry>
)
