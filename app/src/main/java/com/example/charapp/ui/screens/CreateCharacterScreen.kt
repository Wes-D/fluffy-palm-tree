package com.example.charapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateCharacterScreen() {
    var selectedRace by remember { mutableStateOf("Human") }
    var selectedArchetype by remember { mutableStateOf("Innkeeper") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Create Your Character", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(24.dp))

        // Race Selection
        Text(text = "Select Race")
        DropdownMenu(selectedRace, listOf("Human", "Elf", "Dwarf", "Halfling"), onOptionSelected = {
            selectedRace = it
        })

        Spacer(modifier = Modifier.height(16.dp))

        // Archetype Selection
        Text(text = "Select Archetype")
        DropdownMenu(selectedArchetype, listOf("Innkeeper", "Shop Owner", "Farmer", "Street Urchin"), onOptionSelected = {
            selectedArchetype = it
        })

        Spacer(modifier = Modifier.height(24.dp))

        // Placeholder buttons for generating characters
        Button(onClick = { /* Handle Random Generation */ }) {
            Text("Generate Random Character")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { /* Handle Customization */ }) {
            Text("Customize Character")
        }
    }
}


@Composable
fun DropdownMenu(
    selectedOption: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(text = selectedOption)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}


@Composable
fun StatDisplay(statName: String, statValue: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = statName, style = MaterialTheme.typography.headlineMedium)
        Text(text = statValue.toString(), style = MaterialTheme.typography.headlineMedium)
    }
}

fun rollStat(): Int {
    val rolls = List(4) { (1..6).random() }  // Roll four 6-sided dice
    return rolls.sortedDescending().take(3).sum()  // Drop the lowest roll
}

fun generateStats(): Map<String, Int> {
    val stats = listOf("Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma")
    return stats.associateWith { rollStat() }
}





