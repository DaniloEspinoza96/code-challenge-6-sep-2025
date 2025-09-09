package com.example.codechallenge.features.main.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codechallenge.features.main.domain.model.Currency
import com.example.codechallenge.features.main.domain.usecase.GetCurrencyHistoryUseCase
import com.example.codechallenge.features.user.domain.repository.UserRepository
import com.example.codechallenge.features.user.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrencyHistoryUseCase: GetCurrencyHistoryUseCase,
    private val logoutUseCase: LogoutUseCase,
    userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> get() = _state

    fun onStart() {
        onDateChanged(Date().toInstant().toEpochMilli())
    }

    fun onDateChanged(date: Long) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getCurrencyHistoryUseCase(Currency.DOLLAR, date).fold(
                onSuccess = { currencyHistory ->
                    _state.update { it.copy(isLoading = false, dollarHistory = currencyHistory) }
                },
                onFailure = {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = it.errorMessage
                                ?: "Hubo un problema al cargar el histórico del dólar."
                        )
                    }
                })
            getCurrencyHistoryUseCase(Currency.EURO, date).fold(
                onSuccess = { currencyHistory ->
                    _state.update { it.copy(isLoading = false, euroHistory = currencyHistory) }
                },
                onFailure = {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = it.errorMessage
                                ?: "Hubo un problema al cargar el histórico del euro."
                        )
                    }
                })
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }

    val userInfo = userRepository.loggedUser.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )
}