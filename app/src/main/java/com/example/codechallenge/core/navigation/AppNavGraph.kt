package com.example.codechallenge.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.codechallenge.features.main.ui.HomeScreen
import com.example.codechallenge.features.user.ui.LoginScreen
import com.example.codechallenge.features.user.ui.SignUpScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onGoToSignUp = {
                    navController.navigate(Routes.SIGNUP)
                },
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Routes.SIGNUP) {
            SignUpScreen(
                onSignedUp = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = false }
                        launchSingleTop = true
                    }
                },
                onBackToLogin = {
                    val popped = navController.popBackStack()
                    if (!popped) navController.navigate(Routes.LOGIN)
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(0)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}