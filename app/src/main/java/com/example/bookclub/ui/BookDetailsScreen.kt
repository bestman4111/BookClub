package com.example.bookclub.ui

import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    title: String,
    author: String,
    year: String,
    onNavigateBack: () -> Unit,
    onCreateClub: (String, Boolean, String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    var clubName by remember {mutableStateOf("Dezbatere: $title")}
    var isPrivate by remember {mutableStateOf(false)}
    var password by remember {mutableStateOf("")}

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text("Detalii Carte")},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Inapoi")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Scrisa de $author",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Informatii publicare", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Anul primei publicari: $year")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {showDialog = true},
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Creeaza un club pentru aceasta carte", modifier = Modifier.padding(8.dp))
            }
        }
    }

    if(showDialog) {
        AlertDialog(
            onDismissRequest = {showDialog = false},
            title = {Text("Setari Club de Lectura")},
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Organizeaza o camera de dezbateri pentru aceasta carte.")

                    OutlinedTextField(
                        value = clubName,
                        onValueChange = {clubName = it},
                        label = {Text("Numele clubului")},
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Switch(
                            checked = isPrivate,
                            onCheckedChange = {isPrivate = it}
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(if(isPrivate) "Privat (necesita parola)" else "Public (deschis tuturor)")
                    }

                    if (isPrivate) {
                        OutlinedTextField(
                            value = password,
                            onValueChange = {password = it},
                            label = {Text("Parola acces")},
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onCreateClub(clubName, isPrivate, password)
                        showDialog = false
                    }
                ) {
                    Text("Deschide Clubul")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {showDialog = false}
                ) {
                    Text("Anuleaza")
                }
            }
        )
    }
}