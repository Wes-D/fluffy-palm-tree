package com.example.charapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CreateCharacterScreen() {
    var selectedRace by remember { mutableStateOf("Human") }
    var selectedArchetype by remember { mutableStateOf("Innkeeper") }
    var stats by remember { mutableStateOf(generateStats()) }
    var isCustomizationEnabled by remember { mutableStateOf(false) }

    // Scaffold to handle layout
    Scaffold(
        topBar = {
            // You can add a TopBar here if desired
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),  // Adding padding around the whole content
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                // Title with padding and centered
                Text(
                    text = "Create Your Character",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 24.dp)  // Adding padding above and below title
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Race and Archetype selection horizontally
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Race Selection
                    Column {
                        Text(text = "Select Race")
                        DropdownMenu(selectedRace, listOf("Human", "Elf", "Dwarf", "Halfling")) {
                            selectedRace = it
                        }
                    }

                    // Archetype Selection
                    Column {
                        Text(text = "Select Archetype")
                        DropdownMenu(selectedArchetype, listOf("Innkeeper", "Shop Owner", "Farmer", "Street Urchin")) {
                            selectedArchetype = it
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Stats Display with optional sliders
                stats.forEach { (statName, statValue) ->
                    Column {
                        StatDisplay(statName = statName, statValue = statValue)
                        if (isCustomizationEnabled) {
                            EditableStatDisplay(statValue = statValue) { newValue ->
                                stats = stats.toMutableMap().apply { this[statName] = newValue }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Race and Archetype selection horizontally
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Customize Button
                    Button(onClick = { isCustomizationEnabled = true }) {
                        Text("Customize Attributes")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Button to regenerate stats
                    Button(onClick = { stats = generateStats() }) {
                        Text("Generate")
                    }
                }
            }
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

@Composable
fun EditableStatDisplay(
    statValue: Int,
    onValueChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Slider(
            value = statValue.toFloat(),
            onValueChange = { onValueChange(it.toInt()) },
            valueRange = 3f..18f,  // Assuming D&D-like range for stats
            steps = 15  // This will create step values in the range
        )
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





