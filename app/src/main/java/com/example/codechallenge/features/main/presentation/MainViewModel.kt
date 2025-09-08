package com.example.codechallenge.features.main.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codechallenge.features.main.domain.model.Currency
import com.example.codechallenge.features.main.domain.usecase.GetCurrencyHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrencyHistoryUseCase: GetCurrencyHistoryUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    fun onStart() {
        viewModelScope.launch {
            getCurrencyHistoryUseCase(Currency.DOLLAR, Date().toInstant().toEpochMilli()).fold(
                onSuccess = {
                    Log.e(this.javaClass.simpleName, "Success")
                },
                onFailure = {
                    it.message
                    Log.e(this.javaClass.simpleName, "Failure")
                })
            getCurrencyHistoryUseCase(Currency.EURO, Date().toInstant().toEpochMilli())
        }
    }
}