package com.example.charapp

import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val name: String,
    val race: String,
    val archetype: String,
    val stats: Map<String, Int>,
    val traits: List<String>,
    val abilities: List<String>
)
