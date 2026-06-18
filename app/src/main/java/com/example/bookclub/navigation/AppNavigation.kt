package com.example.bookclub.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bookclub.data.UserPreferences
import com.example.bookclub.ui.LoginScreen
import com.example.bookclub.ui.RegisterScreen
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookclub.data.AppDatabase
import kotlinx.coroutines.launch
import com.example.bookclub.data.User
import com.example.bookclub.ui.HomeScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val userPreferences = remember { UserPreferences(context) }
    val database = remember { AppDatabase.getDatabase(context) }
    val coroutineScope = rememberCoroutineScope()

    val userData by userPreferences.userFlow.collectAsState(initial = null)
    if (userData == null) return
    val startDest = if (userData!!.isLoggedIn) "home" else "login"

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {
        composable(route = "login") {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onLoginSuccess = { email, password ->
                    coroutineScope.launch {
                        val user = database.userDao().getUserByEmail(email)
                        if (user != null && user.password == password) {
                            userPreferences.saveUser(user.username)
                            navController.navigate("home") { popUpTo("login") {inclusive = true} }
                        } else {
                            Toast.makeText(context, "Email sau parola incorecte!", Toast.LENGTH_SHORT).show()
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
                onRegisterSuccess = { username, email, password ->
                    coroutineScope.launch {
                        val newUser = User(username = username, email = email, password = password)
                        database.userDao().insertUser(newUser)
                        userPreferences.saveUser(username)
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(route = "home") {
            HomeScreen(
                username = userData?.username ?: "Cititorule",
                onLogout = {
                    coroutineScope.launch {
                        userPreferences.logoutUser()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}