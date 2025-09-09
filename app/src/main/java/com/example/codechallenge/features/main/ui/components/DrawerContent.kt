package com.example.codechallenge.features.main.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.codechallenge.features.main.presentation.MainViewModel
import com.example.codechallenge.features.user.domain.model.UserInfo

@Composable
fun DrawerContent(
    userInfo: UserInfo?,
    viewModel: MainViewModel
) {
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