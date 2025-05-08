package com.testfiles.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.testfiles.viewmodel.SharedViewModel
import java.io.OutputStreamWriter


@Composable
fun RankingScreen(navController: NavController, viewModel: SharedViewModel) {
    val context = LocalContext.current
    val fileUri by viewModel.selectedFileUri.collectAsState()
    val ranking by viewModel.ranking.collectAsState()

    var fileContent by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var message by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(fileUri) {
        isLoading = true
        try {
            fileUri?.let { uri ->
                context.contentResolver.openInputStream(uri)?.use { input ->
                    fileContent = input.bufferedReader().readText()
                } ?: run {
                    message = "Erro: não foi possível abrir o arquivo."
                }
            } ?: run {
                message = "URI nulo"
            }
        } catch (e: Exception) {
            message = "Exceção: ${e.message}"
        }
        isLoading = false
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(16.dp)
        ) {
            CustomHeaderRanking(navController)

            Spacer(modifier = Modifier.height(8.dp))

            ranking.forEachIndexed { index, (item, score) ->
                Text(text = "${index + 1}. $item - Pontos: $score")
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomHeaderRanking(navController: NavController) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
        }
        Text(
            text = "Ranking",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}
