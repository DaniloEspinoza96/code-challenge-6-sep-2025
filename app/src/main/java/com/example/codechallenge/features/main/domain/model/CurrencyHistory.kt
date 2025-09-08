package com.example.codechallenge.features.main.domain.model

data class CurrencyHistory(
    val currency: String,
    val entries: List<CurrencyEntry>
)