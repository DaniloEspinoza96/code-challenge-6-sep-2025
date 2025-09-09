package com.example.codechallenge.features.main.ui.components

import DualLineCurrencyLineChart
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.codechallenge.features.main.domain.model.CurrencyHistory

@Composable
fun CurrencyCard(
    dollarCurrencyHistory: CurrencyHistory?,
    euroCurrencyHistory: CurrencyHistory?,
) {
    Card(
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val dollarColor = Color.Red
            val euroColor = Color.Blue
            Column(
                horizontalAlignment = Alignment.Start
            ) {
                dollarCurrencyHistory?.let {
                    CurrencyLabel(
                        dollarColor,
                        "Valor Dólar (USD): \$${dollarCurrencyHistory.entries.last().value} CLP"
                    )
                }
                euroCurrencyHistory?.let {
                    CurrencyLabel(
                        euroColor,
                        "Valor Euro (EUR): \$${euroCurrencyHistory.entries.last().value} CLP"
                    )
                }
            }

            DualLineCurrencyLineChart(
                historyA = dollarCurrencyHistory,
                historyB = euroCurrencyHistory,
                aLineColor = dollarColor,
                bLineColor = euroColor
            )
        }
    }
}