package com.example.codechallenge.features.main.presentation

import com.example.codechallenge.features.main.domain.model.CurrencyHistory
import com.example.codechallenge.utils.localDateFormatted

data class MainState(
    val isLoading: Boolean = false,
    val dollarHistory: CurrencyHistory? = null,
    val euroHistory: CurrencyHistory? = null,
    val errorMessage: String? = null,
    val selectedDate: String = localDateFormatted()
)