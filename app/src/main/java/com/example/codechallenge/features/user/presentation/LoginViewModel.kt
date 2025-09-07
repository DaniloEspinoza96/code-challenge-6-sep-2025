package com.example.codechallenge.features.user.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codechallenge.features.user.domain.repository.UserRepository
import com.example.codechallenge.features.user.domain.usecase.LoginUseCase
import com.example.codechallenge.features.user.domain.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val logoutUseCase: LogoutUseCase,
    userRepository: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> get() = _state

    val userInfo = userRepository.loggedUser.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    fun onLogin(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase(email = email, password = password).fold(
                onSuccess = {
                    Log.i(javaClass.simpleName, "Successful login")
                },
                onFailure = {
                    Log.e(javaClass.simpleName, it.message!!)
                }
            )
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            logoutUseCase()
        }
    }
}