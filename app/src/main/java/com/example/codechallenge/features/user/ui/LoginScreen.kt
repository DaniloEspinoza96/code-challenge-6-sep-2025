package com.example.codechallenge.features.user.ui

import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.codechallenge.core.ui.components.TopBarCodeChallenge
import com.example.codechallenge.features.user.presentation.LoginEffect
import com.example.codechallenge.features.user.presentation.LoginViewModel
import com.example.codechallenge.features.user.ui.components.LoginContent
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onGoToSignUp: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.userInfo
                .collectLatest { userInfo ->
                    if (userInfo != null) {
                        onLoginSuccess()
                        return@collectLatest
                    }
                }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                LoginEffect.LoggedIn -> onLoginSuccess()
                LoginEffect.GoToSignUp -> onGoToSignUp()
                is LoginEffect.Error -> {
                    snackBarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {},
        topBar = {
            TopBarCodeChallenge(title = "Iniciar sesión")
        }
    ) { padding ->
        LoginContent(padding, viewModel, snackBarHostState)
    }
}