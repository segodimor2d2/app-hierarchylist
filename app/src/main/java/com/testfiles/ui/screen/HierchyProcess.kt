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
    val itemPairs by viewModel.itemPairs.collectAsState()
    val ranking by viewModel.ranking.collectAsState()

    // Inicia direto na análise
    var analyzing by remember { mutableStateOf(true) }
    var currentIndex by remember { mutableStateOf(0) }
    val respostas = remember { mutableStateListOf<Int?>().apply { repeat(itemPairs.size) { add(null) } } }

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

            // Exibe o ranking após o cálculo
            if (!analyzing) {
                Text(
                    text = "Ranking Condorcet:",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                ranking.forEachIndexed { index, (item, score) ->
                    Text(text = "${index + 1}. $item - Pontos: $score")
                }

                return@Column // Não exibe a análise de pares se o ranking foi calculado
            }

            // Análise de pares diretamente ao iniciar
            val total = itemPairs.size
            if (total > 0) {
                val currentPair = itemPairs[currentIndex]
                val selected = respostas[currentIndex]

                Text(
                    text = "Comparação ${currentIndex + 1} de $total",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(currentPair.first)
                    Text("VS")
                    Text(currentPair.second)
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { respostas[currentIndex] = -1 }) { Text("[A - B]") }
                    Button(onClick = { respostas[currentIndex] = 0 }) { Text("[A = B]") }
                    Button(onClick = { respostas[currentIndex] = 1 }) { Text("[A + B]") }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = when (selected) {
                        -1 -> "Você escolheu: [A - B]"
                        0 -> "Você escolheu: [A = B]"
                        1 -> "Você escolheu: [A + B]"
                        else -> "Nenhuma escolha feita."
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { if (currentIndex > 0) currentIndex-- },
                        enabled = currentIndex > 0
                    ) { Text("Anterior") }

                    Button(
                        onClick = { if (currentIndex < total - 1) currentIndex++ },
                        enabled = currentIndex < total - 1
                    ) { Text("Próximo") }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        println("Respostas: $respostas")
                        // Calcula o ranking Condorcet
                        viewModel.calcularRankingCondorcet(respostas)
                        analyzing = false
                        currentIndex = 0
                    },
                    enabled = respostas.none { it == null },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finalizar Comparações")
                }
            } else {
                Text(
                    text = "Nenhum par disponível para comparar.",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyLarge
                )
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
