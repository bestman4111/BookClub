package com.example.bookclub.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun RegisterScreen(
    onNavigateBackToLogin: () -> Unit = {},
    onRegisterSuccess: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var username by remember {mutableStateOf("")}
    var email by remember { mutableStateOf("") }
    var password by remember {mutableStateOf("")}

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Creare cont",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = {username = it},
            label = {Text("Nume utilizator")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {email = it},
            label = {Text("Email")},
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = {Text("Parola")},
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                    onRegisterSuccess(username, email, password)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Creeaza contul")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateBackToLogin) {
            Text("Ai deja cont? Intra aici!")
        }
    }
}