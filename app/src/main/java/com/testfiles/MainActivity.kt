package com.testfiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.rememberNavController
import com.testfiles.navigation.AppNavGraph
import com.testfiles.ui.theme.TestfilesTheme
import com.testfiles.viewmodel.SharedViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializa o ViewModel (mantenha assim)
        val viewModel: SharedViewModel by viewModels()

        setContent {
            TestfilesTheme {
                // 1. NavController deve ser criado dentro do Composition
                val navController = rememberNavController()

                // 2. Passa as dependências para o grafo de navegação
                AppNavGraph(
                    navController = navController,
                    viewModel = viewModel
                )
            }
        }
    }
}
