package com.example.charapp.ui.screens

import androidx.compose.ui.platform.LocalContext
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.content.Context
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import com.example.charapp.Character
import com.example.charapp.CharacterViewModel
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.activity.result.contract.ActivityResultContracts.OpenDocument
import com.example.charapp.generateCharacterPdf
import androidx.core.content.FileProvider
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.widget.Toast
import kotlinx.serialization.encodeToString
import java.io.OutputStream

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SummaryScreen(
    characterViewModel: CharacterViewModel,
    characterName: String,
    race: String,
    archetype: String,
    stats: Map<String, Int>,
    traits: List<String>,
    abilities: List<String>,
    onEditCharacter: () -> Unit,
    onExportCharacter: (Uri?) -> Unit,  // Now we pass the Uri as a parameter
    onLoadCharacter: (Uri?) -> Unit     // Pass Uri for loading the character
) {
    val context = LocalContext.current  // Get the context here inside a @Composable

    // Set up a launcher to create the JSON document
    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
        onResult = { uri: Uri? ->
            onExportCharacter(uri)  // Trigger the export with the chosen URI
        }
    )

    // Set up a launcher to open a JSON document
    val openDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            onLoadCharacter(uri)  // Trigger the load with the selected URI
        }
    )

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

                Spacer(modifier = Modifier.height(48.dp))

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
                        Text(text = "Edit")
                    }

                    Button(onClick = {
                        // Trigger the SAF launcher (CreateDocument) which will pass the URI when the user selects a location
                        createDocumentLauncher.launch("character_data.json")
                    }) {
                        Text("Export")
                    }
                }
                // Load and Save Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = {
                        // Launch the open document intent for loading
                        openDocumentLauncher.launch(arrayOf("application/json"))
                    }) {
                        Text("Load Character")
                    }

                    Button(onClick = {
                        // Launch the create document intent for saving
                        createDocumentLauncher.launch("character_data.json")
                    }) {
                        Text("Save Character")
                    }
                }
            }
        }
    }
}

@Composable
fun SaveCharacterButton(characterViewModel: CharacterViewModel) {
    val context = LocalContext.current

    // Set up a launcher to create the JSON document
    val createJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
        onResult = { uri: Uri? ->
            uri?.let {
                saveCharacterToJson(context, characterViewModel, it)
            }
        }
    )

    Button(onClick = {
        createJsonLauncher.launch("character_data.json")
    }) {
        Text("Save Character")
    }
}

@Composable
fun LoadCharacterButton(onCharacterLoaded: (Character) -> Unit) {
    val context = LocalContext.current

    // Set up a launcher to open a JSON document
    val openJsonLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri: Uri? ->
            uri?.let {
                loadCharacterFromJson(context, it, onCharacterLoaded)
            }
        }
    )

    Button(onClick = {
        openJsonLauncher.launch(arrayOf("application/json"))
    }) {
        Text("Load Character")
    }
}

@Composable
fun ExportToPdfButton(characterViewModel: CharacterViewModel) {
    val context = LocalContext.current

    // Set up a launcher to create the PDF document
    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/pdf"),
        onResult = { uri: Uri? ->
            uri?.let {
                // Save the PDF to the selected location
                generateCharacterPdf(context, characterViewModel, it)
            }
        }
    )

    Button(onClick = {
        createDocumentLauncher.launch("character_summary.pdf")
    }) {
        Text("Export to PDF")
    }
}

fun sharePdf(context: Context, fileName: String) {
    val file = File(context.filesDir, fileName)
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share PDF"))
}

fun saveCharacterToFile(context: Context, character: Character, fileName: String) {
    val jsonString = Json.encodeToString(character)  // Convert character to JSON
    val file = File(context.filesDir, fileName)      // Save in app's internal storage
    file.writeText(jsonString)                       // Write JSON string to file
}

fun loadCharacterFromFile(context: Context, fileName: String): Character? {
    val file = File(context.filesDir, fileName)
    if (file.exists()) {
        val jsonString = file.readText()  // Read JSON string from file
        return Json.decodeFromString(jsonString)  // Convert JSON string back to Character
    }
    return null  // File doesn't exist, return null
}

fun saveCharacterToJson(context: Context, characterViewModel: CharacterViewModel, uri: Uri) {
    val character = Character(
        name = characterViewModel.characterName,
        race = characterViewModel.race,
        archetype = characterViewModel.archetype,
        stats = characterViewModel.stats,
        traits = characterViewModel.traits,
        abilities = characterViewModel.abilities
    )

    // Check if the data is not empty
    if (character.name.isBlank() || character.race.isBlank()) {
        Toast.makeText(context, "Character data is incomplete.", Toast.LENGTH_SHORT).show()
        return
    }

    val jsonString = Json.encodeToString(character)

    try {
        context.contentResolver.openOutputStream(uri)?.use { outputStream ->
            outputStream.write(jsonString.toByteArray())
        }
        Toast.makeText(context, "Character saved successfully", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Failed to save character: ${e.message}", Toast.LENGTH_LONG).show()
    }
}

fun loadCharacterFromJson(context: Context, uri: Uri, onCharacterLoaded: (Character) -> Unit) {
    try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            val character = Json.decodeFromString<Character>(jsonString)
            onCharacterLoaded(character)
            Toast.makeText(context, "Character loaded successfully", Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        Toast.makeText(context, "Failed to load character: ${e.message}", Toast.LENGTH_LONG).show()
    }
}