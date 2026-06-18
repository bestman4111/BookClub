package com.example.bookclub.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookclub.api.BookApiItem
import com.example.bookclub.data.Club

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    username: String,
    clubs: List<Club>,
    onLogout: () -> Unit,
    onBookClick: (String, String, String) -> Unit,
    onClubJoin: (Club) -> Unit,
    viewModel: HomeViewModel = viewModel()
) {
    val books by viewModel.books.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var selectedTab by remember {mutableStateOf(0)}
    val tabs = listOf("Exploreaza carti", "Cluburi active")

    var clubToJoin by remember {mutableStateOf<Club?>(null)}
    var enteredPassword by remember {mutableStateOf("")}
    var isPasswordError by remember {mutableStateOf(false)}

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Salut, $username!") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout", color = MaterialTheme.colorScheme.error)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {selectedTab = index},
                        text = {Text(title)}
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            when(selectedTab) {
                0 -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        when {
                            isLoading -> CircularProgressIndicator()
                            errorMessage != null -> Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error)
                            else -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(books) { bookItem ->
                                        BookItem(bookItem = bookItem, onClick = onBookClick)
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    if (clubs.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Nu s-a creat inca vreun club.")
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(clubs) {club ->
                                ClubItem(club = club, onClick = {clickedClub ->
                                    if(clickedClub.isPrivate) {
                                        clubToJoin = clickedClub
                                        enteredPassword = ""
                                        isPasswordError = false
                                    } else {
                                        onClubJoin(clickedClub)
                                    }
                                })
                            }
                        }
                    }
                }
            }
        }
    }

    if(clubToJoin != null) {
        AlertDialog(
            onDismissRequest = {clubToJoin = null},
            title = {Text("Club Privat")},
            text = {
                Column {
                    Text("Acest club necesita o parola de acces.")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = enteredPassword,
                        onValueChange = {
                            enteredPassword = it
                            isPasswordError = false
                        },
                        label = {Text("Parola")},
                        isError = isPasswordError,
                        singleLine = true
                    )
                    if(isPasswordError) {
                        Text("Parola incorecta!", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if(enteredPassword == clubToJoin?.password) {
                        onClubJoin(clubToJoin!!)
                        clubToJoin = null
                    } else {
                        isPasswordError = true
                    }
                }) {Text("Intra")}
            },
            dismissButton = {
                TextButton(onClick = {clubToJoin = null}) { Text("Anuleaza") }
            }
        )
    }
}

@Composable
fun ClubItem(club: Club, onClick: (Club) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = {onClick(club)}
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = club.name, style = MaterialTheme.typography.titleLarge, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
            Text(text = "Pentru cartea ${club.bookTitle}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Spacer(modifier = Modifier.height(8.dp))

            val isPrivateText = if (club.isPrivate) "Club Privat (necesita parola)" else "Club Public"
            val badgeColor = if (club.isPrivate) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary

            Text(
                text = isPrivateText,
                style = MaterialTheme.typography.labelMedium,
                color = badgeColor,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
        }
    }
}

@Composable
fun BookItem(bookItem: BookApiItem, onClick: (String, String, String) -> Unit) {
    val authorText = bookItem.authorName?.joinToString(", ") ?: "Autor necunoscut"
    val yearText = bookItem.firstPublishYear?.toString() ?: "Indisponibil"

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = {
            onClick(bookItem.title, authorText, yearText)
        }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = bookItem.title, style = MaterialTheme.typography.titleLarge)

            Text(
                text = "de $authorText",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = yearText, style = MaterialTheme.typography.bodyMedium)
        }
    }
}