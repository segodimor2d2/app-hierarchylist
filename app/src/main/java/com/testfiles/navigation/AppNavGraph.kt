package com.testfiles.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.testfiles.ui.screen.EditScreen
import com.testfiles.ui.screen.HomeScreen
import com.testfiles.ui.screen.CompareScreen
import com.testfiles.ui.screen.RankingScreen
import com.testfiles.viewmodel.SharedViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: SharedViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "home"  // Mantém a Home como tela inicial
    ) {
        // Tela Home (original)
        composable("home") {
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // Tela de Edição (original)
        composable("edit") {
            EditScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // Nova tela de Comparation
        composable("compare") {
            CompareScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // Nova tela de Ranking
        composable("ranking") {
            RankingScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}