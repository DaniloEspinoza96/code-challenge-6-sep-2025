package com.example.codechallenge.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBarCodeChallenge(
    title: String,
    leftIcon: ImageVector? = null,
    onLeftIconClick: () -> Unit = {},
    leftIconContentDescription: String? = null
) {
    TopAppBar(
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) { Text(title, modifier = Modifier.offset(x = (-14).dp)) }
        },
        navigationIcon = {
            leftIcon?.let {
                IconButton(
                    onClick = onLeftIconClick
                ) {
                    Icon(
                        leftIcon,
                        contentDescription = leftIconContentDescription ?: ""
                    )
                }
            }
        }
    )
}