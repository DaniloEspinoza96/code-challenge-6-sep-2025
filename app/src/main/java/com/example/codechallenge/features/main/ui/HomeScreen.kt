package com.example.codechallenge.features.main.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.codechallenge.features.main.presentation.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop

@Composable
fun HomeScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            viewModel.userInfo
                .drop(1)
                .collectLatest { userInfo ->
                    if (userInfo == null) {
                        onLogout()
                        return@collectLatest
                    }
                }
        }
    }
    Column {
        Text("En pantalla principal")
        Button(onClick = viewModel::onLogout) { Text("Logout") }
    }
}