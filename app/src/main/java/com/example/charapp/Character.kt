package com.example.charapp

data class Character(
    val name: String,
    val race: String,
    val archetype: String,
    val stats: Map<String, Int>,
    val traits: List<String>,
    val abilities: List<String>
)
