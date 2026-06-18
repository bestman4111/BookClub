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
import com.example.bookclub.ui.BookDetailsScreen
import com.example.bookclub.ui.HomeScreen
import kotlinx.coroutines.flow.compose
import java.net.URLEncoder

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
            val clubs by database.clubDao().getAllClubs().collectAsState(initial = emptyList())

            HomeScreen(
                username = userData?.username ?: "Cititorule",
                clubs = clubs,
                onLogout = {
                    coroutineScope.launch {
                        userPreferences.logoutUser()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                },
                onBookClick = { title, author, year ->
                    val encodedTitle = URLEncoder.encode(title, "UTF-8")
                    val encodedAuthor = URLEncoder.encode(author, "UTF-8")
                    val encodedYear = URLEncoder.encode(year, "UTF-8")

                    navController.navigate("details/$encodedTitle/$encodedAuthor/$encodedYear")
                }
            )
        }

        composable(
            route = "details/{title}/{author}/{year}",
            arguments = listOf(
                androidx.navigation.navArgument("title") {type = androidx.navigation.NavType.StringType},
                androidx.navigation.navArgument("author") {type = androidx.navigation.NavType.StringType},
                androidx.navigation.navArgument("year") {type = androidx.navigation.NavType.StringType}
            )
        ) { backStackEntry ->
            val title = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("title") ?: "", "UTF-8")
            val author = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("author") ?: "", "UTF-8")
            val year = java.net.URLDecoder.decode(backStackEntry.arguments?.getString("year") ?: "", "UTF-8")

            BookDetailsScreen(
                title = title,
                author = author,
                year = year,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onCreateClub = {clubName, isPrivate, clubPassword ->
                    coroutineScope.launch {
                        val newClub = com.example.bookclub.data.Club(
                            name = clubName,
                            bookTitle = title,
                            isPrivate = isPrivate,
                            password = clubPassword
                        )

                        database.clubDao().insertClub(newClub)

                        Toast.makeText(context, "Clubul '$clubName' a fost creat!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}