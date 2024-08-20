package com.example.charapp

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class CharacterViewModel : ViewModel() {
    var characterName by mutableStateOf("")
    var race by mutableStateOf("")
    var archetype by mutableStateOf("")
    var stats by mutableStateOf(mapOf<String, Int>())
    var traits by mutableStateOf(listOf<String>())
    var abilities by mutableStateOf(listOf<String>())

    // Function to generate or update the character details
    fun updateCharacter(
        name: String,
        race: String,
        archetype: String,
        stats: Map<String, Int>,
        traits: List<String>,
        abilities: List<String>
    ) {
        characterName = name
        this.race = race
        this.archetype = archetype
        this.stats = stats
        this.traits = traits
        this.abilities = abilities
    }
}
