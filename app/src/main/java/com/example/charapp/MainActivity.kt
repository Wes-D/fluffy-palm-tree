package com.example.charapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.charapp.ui.theme.CharAppTheme
import com.example.charapp.ui.screens.HomeScreen
import com.example.charapp.ui.screens.CreateCharacterScreen
import com.example.charapp.ui.screens.LoadCharacterScreen



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
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onCreateNewCharacter = { navController.navigate("create_character") },
                onLoadCharacter = { navController.navigate("load_character") }
            )
        }
        composable("create_character") {
            CreateCharacterScreen()
        }
        composable("load_character") {
            LoadCharacterScreen()
        }
    }
}
