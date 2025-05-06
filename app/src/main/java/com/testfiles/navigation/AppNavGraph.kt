package com.testfiles.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.testfiles.ui.screen.EditScreen
import com.testfiles.ui.screen.HomeScreen
import com.testfiles.viewmodel.SharedViewModel

@Composable
fun AppNavGraph(navController: NavHostController, viewModel: SharedViewModel) {
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable("edit") {
            EditScreen(navController = navController, viewModel = viewModel)
        }
    }
}
