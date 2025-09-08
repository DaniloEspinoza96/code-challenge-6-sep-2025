package com.example.codechallenge.features.main.presentation

import com.example.codechallenge.features.main.domain.model.CurrencyHistory

data class MainState(
    val isLoading: Boolean = false,
    val dollarHistory: CurrencyHistory? = null,
    val euroHistory: CurrencyHistory? = null,
    val errorMessage: String? = null
)