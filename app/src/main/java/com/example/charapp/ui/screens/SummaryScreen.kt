package com.example.charapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    characterName: String,
    race: String,
    archetype: String,
    stats: Map<String, Int>,
    traits: List<String>,
    abilities: List<String>,
    onEditCharacter: () -> Unit,
    onExportCharacter: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Character Summary") }
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Text(text = "Character Overview", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(16.dp))

                // Name, Race, Archetype
                Text(text = "Name: $characterName", style = MaterialTheme.typography.headlineMedium)
                Text(text = "Race: $race", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Archetype: $archetype", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(24.dp))

                // Stats
                Text(text = "Stats", style = MaterialTheme.typography.headlineMedium)
                stats.forEach { (statName, statValue) ->
                    Text(text = "$statName: $statValue", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Traits
                Text(text = "Traits", style = MaterialTheme.typography.headlineMedium)
                traits.forEach { trait ->
                    Text(text = trait, style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Abilities
                Text(text = "Abilities", style = MaterialTheme.typography.headlineMedium)
                abilities.forEach { ability ->
                    Text(text = ability, style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Edit and Export Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = onEditCharacter) {
                        Text(text = "Edit Character")
                    }

                    Button(onClick = onExportCharacter) {
                        Text(text = "Export Character")
                    }
                }
            }
        }
    }
}
