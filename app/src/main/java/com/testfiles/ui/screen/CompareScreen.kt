package com.testfiles.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.testfiles.viewmodel.SharedViewModel
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState





@Composable
fun CompareScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    val itemPairs by viewModel.itemPairs.collectAsState()

    // Inicia direto na análise
    var analyzing by remember { mutableStateOf(true) }
    var currentIndex by remember { mutableStateOf(0) }
    val respostas = remember { mutableStateListOf<Int?>().apply { repeat(itemPairs.size) { add(null) } } }
    val userAnswers = remember { mutableStateListOf<String?>() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 16.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clickable { navController.popBackStack() }
                            .padding(vertical = 16.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }

                    Text(
                        text = "Proceso de Hierarquia",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }

                val total = itemPairs.size

                if (total > 0) {
                    val optionColors = mapOf(
                        -1 to Color(0xFFEF5350), // vermelho
                        0 to Color(0xFF42A5F5),  // azul
                        1 to Color(0xFF66BB6A)   // verde
                    )
                    val pagerState = rememberPagerState(initialPage = 0, pageCount = { total })

                    val currentPage = pagerState.currentPage
                    val selected = respostas.getOrNull(currentPage)
                    val currentPair = itemPairs.getOrNull(currentPage)

                    val currentColor = optionColors[selected] ?: Color.Unspecified

                    Text(
                        text = "Comparação ${currentPage + 1} de $total",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = when (selected) {
                            -1 -> "Você escolheu: ${currentPair?.first} > ${currentPair?.second}"
                            0 -> "Você escolheu: ${currentPair?.first} = ${currentPair?.second}"
                            1 -> "Você escolheu: ${currentPair?.first} < ${currentPair?.second}"
                            else -> "Nenhuma escolha feita."
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = currentColor
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    HorizontalPager(
                        state = pagerState
                    ) { page ->
                        val currentPair = itemPairs[page]

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 16.dp)
                        ) {
                            Button(
                                onClick = { respostas[page] = -1 },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RectangleShape,
                                border = if (respostas[page] == -1) BorderStroke(2.dp, Color.White) else null,
                                colors = ButtonDefaults.buttonColors(containerColor = optionColors[-1]!!)
                            ) {
                                Text(
                                    text = "${currentPair.first}\né MAIS importante do que\n${currentPair.second}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                            Button(
                                onClick = { respostas[page] = 0 },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RectangleShape,
                                border = if (respostas[page] == 0) BorderStroke(2.dp, Color.White) else null,
                                colors = ButtonDefaults.buttonColors(containerColor = optionColors[0]!!)
                            ) {
                                Text(
                                    text = "${currentPair.first}\né IGUAL de importante do que\n${currentPair.second}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                            Button(
                                onClick = { respostas[page] = 1 },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RectangleShape,
                                border = if (respostas[page] == 1) BorderStroke(2.dp, Color.White) else null,
                                colors = ButtonDefaults.buttonColors(containerColor = optionColors[1]!!)
                            ) {
                                Text(
                                    text = "${currentPair.first}\né MENOS importante do que\n${currentPair.second}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Aqui criamos a variável que monitora se todas as respostas foram feitas
                    val allAnswered = respostas.none { it == null }

                    Button(
                        onClick = {
                            println("Respostas: $respostas")
                            viewModel.calcularRankingCondorcet(respostas)
                            navController.navigate("ranking")
                            analyzing = false
                        },
                        enabled = allAnswered,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (allAnswered) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    ) {
                        Text("Finalizar")
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
}
