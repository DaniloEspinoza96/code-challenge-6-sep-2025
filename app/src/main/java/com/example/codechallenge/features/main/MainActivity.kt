package com.example.codechallenge.features.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.codechallenge.core.ui.theme.CodeChallengeTheme
import com.example.codechallenge.features.user.presentation.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            LaunchedEffect(signUpViewModel) {
                signUpViewModel.onSignUp("daniloespinoza96@gmail.com", "Danilo", "Espinoza", "lol123")
            }
            CodeChallengeTheme {
                val state by signUpViewModel.state.collectAsState()
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    when {
                        state.isLoading -> {
                            Text("Is Loading")
                        }
                        state.success == true -> {
                            Text("User created successfully")
                        }
                        state.errorMessage != null -> {
                            Text(state.errorMessage!!)
                        }
                    }
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CodeChallengeTheme {
        Greeting("Android")
    }
}