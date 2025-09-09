package com.example.codechallenge.features.main.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.codechallenge.core.ui.components.DatePickerModalInput
import com.example.codechallenge.features.main.presentation.MainViewModel

@Composable
fun HomeContent(
    padding: PaddingValues,
    viewModel: MainViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showDateModalPicker by rememberSaveable { mutableStateOf(false) }

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

                Text(
                    state.selectedDate,
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