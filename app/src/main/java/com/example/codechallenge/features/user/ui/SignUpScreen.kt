package com.example.codechallenge.features.user.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.codechallenge.core.ui.components.TopBarCodeChallenge
import com.example.codechallenge.features.user.presentation.SignUpEffect
import com.example.codechallenge.features.user.presentation.SignUpViewModel
import com.example.codechallenge.features.user.ui.components.SignUpContent

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onBackToLogin: () -> Unit,
    onSignedUp: () -> Unit
) {
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                SignUpEffect.SignedUp -> onSignedUp()
                SignUpEffect.OnGoToLogin -> onBackToLogin()
                is SignUpEffect.Error -> {
                    snackBarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {},
        topBar = {
            TopBarCodeChallenge(
                title = "Registrarse",
                leftIcon = Icons.AutoMirrored.Filled.ArrowBack,
                leftIconContentDescription = "Volver a inicio de sesión",
                onLeftIconClick = { viewModel.onGoToLogin() }
            )
        }
    ) { padding ->
        SignUpContent(padding, viewModel, snackBarHostState)
    }
}