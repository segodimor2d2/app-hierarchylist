package com.testfiles.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.testfiles.viewmodel.SharedViewModel
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    val itemPairs by viewModel.itemPairs.collectAsState()
    val respostas = remember { mutableStateListOf<Int?>().apply { repeat(itemPairs.size) { add(null) } } }
    val scope = rememberCoroutineScope()

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
                // Top bar
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
                        -1 to Color.Gray,
                        1 to Color.Gray
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


                    Spacer(modifier = Modifier.height(8.dp))

                    // SLIDER
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f) // ocupa o espaço disponível
                    ) { page ->
                        val currentPair = itemPairs[page]

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { respostas[page] = -1 },
                                modifier = Modifier.weight(1f), // ocupa metade do espaço
                                shape = RectangleShape,
                                border = if (respostas[page] == -1) BorderStroke(2.dp, Color.White) else null,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (respostas[page] == -1)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        optionColors[-1]!!
                                )

                            ) {
                                Text(
                                    text = "${currentPair.first}\né MAIS importante do que\n${currentPair.second}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                            Button(
                                onClick = { respostas[page] = 1 },
                                modifier = Modifier.weight(1f), // ocupa metade do espaço
                                shape = RectangleShape,
                                border = if (respostas[page] == 1) BorderStroke(2.dp, Color.White) else null,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (respostas[page] == 1)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        optionColors[-1]!!
                                )
                            ) {
                                Text(
                                    text = "${currentPair.first}\né MENOS importante do que\n${currentPair.second}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }

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

                    Spacer(modifier = Modifier.height(12.dp))

                    // ÁREA DE SWIPE
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(Color(0xFFE0E0E0)) // cinza claro para visualização
                            .pointerInput(currentPage) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    val (dx, dy) = dragAmount
                                    val absDx = abs(dx)
                                    val absDy = abs(dy)

                                    val isHorizontal = absDx > absDy
                                    val nextPage = currentPage + 1

                                    if (isHorizontal) {
                                        if (dx > 0) {
                                            respostas[currentPage] = 1 // Swipe para direita
                                        } else {
                                            respostas[currentPage] = -1 // Swipe para esquerda
                                        }
                                    } else {
                                        if (dy < 0) {
                                            respostas[currentPage] = 0 // Swipe para cima
                                        } else if (dy > 0) {
                                            respostas[currentPage] = 0 // Swipe para baixo (adicionado)
                                        }
                                    }

                                    if (nextPage < total) {
                                        scope.launch {
                                            pagerState.animateScrollToPage(nextPage)
                                        }
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Deslize aqui para escolher", color = Color.DarkGray)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val allAnswered = respostas.none { it == null }

                    Button(
                        onClick = {
                            println("Respostas: $respostas")
                            viewModel.calcularRankingCondorcet(respostas)
                            navController.navigate("ranking")
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
