package com.example.bookclub.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookclub.data.UserPreferences
import com.example.bookclub.ui.LoginScreen
import com.example.bookclub.ui.RegisterScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val userPreferences = remember { UserPreferences(context) }

    val coroutineScope = rememberCoroutineScope()

    val userData by userPreferences.userFlow.collectAsState(initial = null)

    if (userData == null) return

    val startDest = if (userData!!.isLoggedIn) "home" else "login"

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable(route = "login") {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onLoginSuccess = { email ->
                    coroutineScope.launch {
                        val generatedUsername = email.substringBefore("@")
                        userPreferences.saveUser(generatedUsername)

                        navController.navigate("home"){
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(route = "register") {
            RegisterScreen(
                onNavigateBackToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = { username ->
                    coroutineScope.launch {
                        userPreferences.saveUser(username)

                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(route = "home") {
            Text("Bine ai venit, ${userData?.username}! Aici va fi lista de cluburi.")
        }
    }
}