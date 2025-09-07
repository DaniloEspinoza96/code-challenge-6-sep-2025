package com.example.codechallenge.features.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.codechallenge.features.user.domain.model.User
import com.example.codechallenge.features.user.domain.usecase.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    private val _state: MutableStateFlow<SignUpState> = MutableStateFlow(SignUpState())
    val state: StateFlow<SignUpState> get() = _state

    fun onSignUp(email: String, name: String, lastName: String, password: String) {
        viewModelScope.launch {
            _state.value = SignUpState(isLoading = true)
            signUpUseCase(
                User(
                    name = name,
                    lastname = lastName,
                    email = email,
                    password = password
                )
            ).fold(
                onSuccess = {
                    _state.value = SignUpState(
                        success = true
                    )
                },
                onFailure = {
                    _state.value = SignUpState(
                        success = false,
                        errorMessage = it.message
                    )
                }
            )
        }
    }
}