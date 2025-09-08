package com.example.codechallenge.features.user.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.codechallenge.BuildConfig
import com.example.codechallenge.features.user.presentation.SignUpEffect
import com.example.codechallenge.features.user.presentation.SignUpViewModel
import com.example.codechallenge.features.user.presentation.UiError

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onBackToLogin: () -> Unit,
    onSignedUp: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(Unit) {
        viewModel.effects.collect{effect ->
            when (effect) {
                SignUpEffect.SignedUp -> onBackToLogin()
                SignUpEffect.OnGoToLogin -> onBackToLogin()
                is SignUpEffect.Error -> {
                    snackBarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    Column {
        Text("En pantalla signup")
        Button(onClick = { onBackToLogin() }) { Text("ir a login") }
        Button(onClick = { onSignedUp() }) { Text("registrarse") }
    }

    Scaffold(
        snackbarHost = {}
    ) { padding ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Name section
                OutlinedTextField(
                    value = state.name,
                    onValueChange = viewModel::onNameChange,
                    label = { Text("Nombre") },
                    isError = state.nameError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                state.nameError?.let { uiError ->
                    val message = when (uiError) {
                        UiError.REQUIRED -> "Nombre requerido."
                        UiError.INVALID -> "Nombre inválido."
                    }
                    Text(message, color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(12.dp))

                // LastName section
                OutlinedTextField(
                    value = state.lastname,
                    onValueChange = viewModel::onLastNameChange,
                    label = { Text("Apellido") },
                    isError = state.nameError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                state.lastnameError?.let { uiError ->
                    val message = when (uiError) {
                        UiError.REQUIRED -> "Apellido requerido."
                        UiError.INVALID -> "Apellido inválido."
                    }
                    Text(message, color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(12.dp))

                // Email section
                OutlinedTextField(
                    value = state.email,
                    onValueChange = viewModel::onEmailChange,
                    label = { Text("Correo") },
                    isError = state.emailError != null,
                    modifier = Modifier.fillMaxWidth()
                )
                state.emailError?.let { uiError ->
                    val message = when (uiError) {
                        UiError.REQUIRED -> "Correo requerido."
                        UiError.INVALID -> "Correo electrónico inválido."
                    }
                    Text(message, color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(12.dp))

                // Password section
                OutlinedTextField(
                    value = state.password,
                    onValueChange = viewModel::onPasswordChange,
                    label = { Text("Contraseña") },
                    isError = state.passwordError != null,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                state.passwordError?.let { uiError ->
                    val message = when (uiError) {
                        UiError.REQUIRED -> "Contraseña requerida."
                        UiError.INVALID -> "El largo mínimo de la contraseña es de ${BuildConfig.MINIMUM_PASSWORD_LENGTH}."
                    }
                    Text(message, color = MaterialTheme.colorScheme.error)
                }

                // ConfirmPassword section
                OutlinedTextField(
                    value = state.confirmPassword,
                    onValueChange = viewModel::onConfirmPasswordChange,
                    label = { Text("Confirmar contraseña") },
                    isError = state.passwordError != null,
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )
                state.confirmPasswordError?.let { uiError ->
                    val message = when (uiError) {
                        UiError.REQUIRED -> "Confirmar contraseña requerido."
                        UiError.INVALID -> "Las contraseñas deben coincidir"
                    }
                    Text(message, color = MaterialTheme.colorScheme.error)
                }

                Spacer(Modifier.height(12.dp))

                Button(
                    onClick = viewModel::onSubmit,
                    enabled = state.canSubmit,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.isSubmitting) {
                        CircularProgressIndicator(
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                    }
                    Text(if (state.isSubmitting) "Registrando..." else "Registrarse")
                }

                TextButton(
                    onClick = viewModel::onGoToLogin,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver al Inicio de sesión")
                }
            }

            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .windowInsetsPadding(WindowInsets.statusBars)
            )
        }
    }
}