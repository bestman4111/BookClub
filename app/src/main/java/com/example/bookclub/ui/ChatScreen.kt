package com.example.bookclub.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.bookclub.data.Message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    clubName: String,
    username: String,
    messages: List<Message>,
    onSendMessage: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var messageText by remember { mutableStateOf("")}

    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(clubName)},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Inapoi")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = {messageText = it},
                        modifier = Modifier.weight(1f),
                        placeholder = {Text("Scrie un mesaj...")},
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if(messageText.isNotBlank()) {
                                onSendMessage(messageText)
                                messageText = ""
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Trimite", tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (messages.isEmpty()) {
                item {
                    Text("Nu exista mesaje inca. Fii cel care sparge gheata!", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            items(messages) { msg ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(text = msg.senderName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = msg.text, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        }
    }
}