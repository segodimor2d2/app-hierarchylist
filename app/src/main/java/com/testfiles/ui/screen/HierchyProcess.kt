package com.testfiles.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.testfiles.viewmodel.SharedViewModel

@Composable
fun HierarchyProcessScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    // Observa os dados processados do ViewModel
    val processedData by viewModel.processedData.collectAsState()

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
            CustomHeaderHiechyProcess(navController)

            Spacer(modifier = Modifier.height(24.dp))

            // Conteúdo do arquivo com scroll
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = processedData ?: "Nenhum dado disponível",
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botão para análise adicional (opcional)
            Button(
                onClick = { /* Lógica de análise adicional */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Analisar")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomHeaderHiechyProcess(navController: NavController) {
    Row(
        modifier = Modifier.padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
        }
        Text(
            text = "Hierchy Proccess",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}
