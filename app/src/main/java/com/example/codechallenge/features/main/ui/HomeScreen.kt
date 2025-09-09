package com.example.codechallenge.features.main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.example.codechallenge.core.ui.components.DatePickerModalInput
import com.example.codechallenge.features.main.presentation.MainViewModel
import com.example.codechallenge.features.main.ui.components.CurrencyCard
import com.example.codechallenge.utils.localDateFormatted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val state by viewModel.state.collectAsStateWithLifecycle()
    val userInfo by viewModel.userInfo.collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var showDateModalPicker by rememberSaveable { mutableStateOf(false) }

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
            ModalDrawerSheet(modifier = Modifier.wrapContentWidth()) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text("Hola, ${userInfo?.name ?: ""} ${userInfo?.lastname ?: ""}!")
                        Text(userInfo?.email ?: "")
                    }

                    TextButton(
                        onClick = viewModel::onLogout,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    ) { Text("Cerrar sesión") }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Indicadores Económicos",
                                modifier = Modifier.offset(x = (-14).dp)
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } }
                        ) { Icon(Icons.Default.Menu, contentDescription = "Abrir menú") }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                OutlinedButton(
                    onClick = { showDateModalPicker = true },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    ConstraintLayout {
                        val (text, icon) = createRefs()

                        Text(state.selectedDate,
                            modifier = Modifier.constrainAs(text) {
                                start.linkTo(parent.start)
                                end.linkTo(icon.start)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            })
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Cambiar fecha",
                            modifier = Modifier
                                .constrainAs(icon) {
                                    start.linkTo(text.end)
                                    end.linkTo(parent.end)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                }
                                .padding(start = 8.dp)
                        )
                    }
                }

                if (showDateModalPicker) {
                    DatePickerModalInput(
                        onDismiss = { showDateModalPicker = false },
                        onDateSelected = { dateInMillis ->
                            dateInMillis?.let {
                                viewModel.onDateChanged(it)
                            }
                        }
                    )
                }

                CurrencyCard(state.dollarHistory, state.euroHistory)
            }
        }
    }
}