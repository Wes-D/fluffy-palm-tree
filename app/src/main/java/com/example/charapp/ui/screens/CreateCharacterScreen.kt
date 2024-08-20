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
    var generatedName by remember { mutableStateOf(generateRandomName(selectedRace)) }
    var traitsAndAbilities by remember { mutableStateOf(generateTraitsAndAbilities(selectedRace, selectedArchetype)) }


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
                            traitsAndAbilities = generateTraitsAndAbilities(it, selectedArchetype)
                            generatedName = generateRandomName(selectedRace)
                            stats = generateStats()
                        }
                    }

                    // Archetype Selection
                    Column {
                        Text(text = "Select Archetype")
                        DropdownMenu(selectedArchetype, listOf("Innkeeper", "Shop Owner", "Farmer", "Street Urchin")) {
                            selectedArchetype = it
                            traitsAndAbilities = generateTraitsAndAbilities(selectedRace, it)
                            stats = generateStats()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = generatedName, style = MaterialTheme.typography.headlineMedium)

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
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = "Traits: ${traitsAndAbilities.first.joinToString()}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Abilities: ${traitsAndAbilities.second.joinToString()}", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Customize Button
                    Button(onClick = { isCustomizationEnabled = !isCustomizationEnabled }) {
                        Text("Customize Attributes")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Button to regenerate stats
                    Button(onClick = {
                        stats = generateStats()
                        generatedName = generateRandomName(selectedRace)
                        traitsAndAbilities = generateTraitsAndAbilities(selectedRace, selectedArchetype)
                    }) {
                        Text("Re-roll")
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
        Text(text = statName, style = MaterialTheme.typography.headlineSmall)
        Text(text = statValue.toString(), style = MaterialTheme.typography.headlineSmall)
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

fun generateRandomName(race: String): String {
    val humanNames = listOf("John", "Jane", "Michael", "Sarah")
    val elfNames = listOf("Aelar", "Faen", "Eldrin", "Lura")
    val dwarfNames = listOf("Thrain", "Bron", "Kara", "Durin")
    val halflingNames = listOf("Perrin", "Lila", "Milo", "Rosie")

    return when (race) {
        "Human" -> humanNames.random()
        "Elf" -> elfNames.random()
        "Dwarf" -> dwarfNames.random()
        "Halfling" -> halflingNames.random()
        else -> "Unknown"
    }
}

fun generateTraitsAndAbilities(race: String, archetype: String): Pair<List<String>, List<String>> {
    val raceTraits = mapOf(
        "Human" to listOf("Adaptability", "Versatility"),
        "Elf" to listOf("Keen Senses", "Fey Ancestry"),
        "Dwarf" to listOf("Resilience", "Stonecunning"),
        "Halfling" to listOf("Luck", "Bravery")
    )

    val archetypeAbilities = mapOf(
        "Innkeeper" to listOf("Hospitality", "Insight"),
        "Shop Owner" to listOf("Business Acumen", "Persuasion"),
        "Farmer" to listOf("Endurance", "Animal Handling"),
        "Street Urchin" to listOf("Stealth", "Survival")
    )

    val traits = raceTraits[race] ?: emptyList()
    val abilities = archetypeAbilities[archetype] ?: emptyList()

    return traits to abilities
}



