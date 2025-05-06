package com.testfiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.testfiles.ui.HomeScreen
import com.testfiles.ui.EditScreen
import com.testfiles.ui.theme.TestfilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestfilesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(navController)
                        }
                        composable(
                            "edit?uri={uri}",
                            arguments = listOf(navArgument("uri") { defaultValue = "" })
                        ) { backStackEntry ->
                            val uriString = backStackEntry.arguments?.getString("uri") ?: ""
                            EditScreen(uriString)
                        }
                    }
                }
            }
        }
    }
}
