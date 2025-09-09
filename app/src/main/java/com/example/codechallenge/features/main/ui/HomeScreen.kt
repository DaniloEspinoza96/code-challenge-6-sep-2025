package com.example.codechallenge.features.main.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.codechallenge.core.ui.components.TopBarCodeChallenge
import com.example.codechallenge.features.main.presentation.MainViewModel
import com.example.codechallenge.features.main.ui.components.DrawerContent
import com.example.codechallenge.features.main.ui.components.HomeContent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val userInfo by viewModel.userInfo.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.onStart()

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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(userInfo, viewModel)
        }
    ) {
        Scaffold(
            topBar = {
                TopBarCodeChallenge(
                    title = "Indicadores Económicos",
                    leftIcon = Icons.Default.Menu,
                    leftIconContentDescription = "Abrir menú",
                    onLeftIconClick = { scope.launch { drawerState.open() } }
                )
            }
        ) { padding ->
            HomeContent(padding, viewModel)
        }
    }
}
