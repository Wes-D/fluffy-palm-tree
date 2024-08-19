package com.example.charapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(onCreateNewCharacter: () -> Unit, onLoadCharacter: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to CharApp", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onCreateNewCharacter) {
            Text("Create New Character")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onLoadCharacter) {
            Text("Load Character")
        }
    }
}
