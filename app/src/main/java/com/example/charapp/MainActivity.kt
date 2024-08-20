package com.example.charapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.charapp.ui.theme.CharAppTheme
import com.example.charapp.ui.screens.HomeScreen
import com.example.charapp.ui.screens.CreateCharacterScreen
import com.example.charapp.ui.screens.LoadCharacterScreen
import com.example.charapp.ui.screens.SummaryScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CharAppTheme {
                Surface {
                    val navController = rememberNavController()
                    AppNavGraph(navController = navController)
                }
            }
        }
    }
}

@Composable
fun AppNavGraph(navController: NavHostController, characterViewModel: CharacterViewModel = viewModel()) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onCreateNewCharacter = { navController.navigate("create_character") },
                onLoadCharacter = { navController.navigate("load_character") }
            )
        }
        composable("create_character") {
            CreateCharacterScreen(
                characterViewModel = characterViewModel,
                onProceedToSummary = { character ->
                    // Update the character data in the ViewModel
                    characterViewModel.updateCharacter(
                        name = character.name,
                        race = character.race,
                        archetype = character.archetype,
                        stats = character.stats,
                        traits = character.traits,
                        abilities = character.abilities
                    )

                    navController.navigate("summary_screen")
                }
            )
        }

        composable("summary_screen") {
            SummaryScreen(
                characterName = characterViewModel.characterName,
                race = characterViewModel.race,
                archetype = characterViewModel.archetype,
                stats = characterViewModel.stats,
                traits = characterViewModel.traits,
                abilities = characterViewModel.abilities,
                onEditCharacter = { navController.popBackStack() },  // Pop back to the previous screen
                onExportCharacter = { /* Export logic here */ }
            )
        }

        composable("load_character") {
            LoadCharacterScreen()
        }
    }
}