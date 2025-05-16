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
    val respostas = remember {
        mutableStateListOf<Int?>().apply {
            repeat(itemPairs.size) { add(null) }
        }
    }
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
            Column(modifier = Modifier.fillMaxSize()) {
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

                    // Define resposta como 0 (empate) ao entrar na página se ainda for null
                    LaunchedEffect(currentPage) {
                        if (respostas[currentPage] == null) {
                            respostas[currentPage] = 0
                        }
                    }

                    val selected = respostas.getOrNull(currentPage)
                    val currentPair = itemPairs.getOrNull(currentPage)
                    val currentColor = optionColors[selected] ?: Color.Unspecified

                    Text(
                        text = "Comparação ${currentPage + 1} de $total",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f)
                    ) { page ->
                        val currentPair = itemPairs[page]

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {
                                    respostas[page] = if (respostas[page] == -1) 0 else -1
                                },
                                modifier = Modifier.fillMaxWidth(),
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
                                onClick = {
                                    respostas[page] = if (respostas[page] == 1) 0 else 1
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RectangleShape,
                                border = if (respostas[page] == 1) BorderStroke(2.dp, Color.White) else null,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (respostas[page] == 1)
                                        MaterialTheme.colorScheme.primary
                                    else
                                        optionColors[1]!!
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
                            1 -> "Você escolheu: ${currentPair?.first} < ${currentPair?.second}"
                            0 -> "Você escolheu: ${currentPair?.first} = ${currentPair?.second}"
                            else -> "Nenhuma escolha feita."
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        color = currentColor
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .background(Color(0xFFE0E0E0))
                            .pointerInput(currentPage) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    val (dx, dy) = dragAmount
                                    val absDx = abs(dx)
                                    val absDy = abs(dy)

                                    val isHorizontal = absDx > absDy
                                    val isVertical = absDy > absDx
                                    val nextPage = currentPage + 1

                                    if (isHorizontal) {
                                        if (dx > 0) {
                                            respostas[currentPage] = 0 // direita
                                        } else {
                                            respostas[currentPage] = 0 // esquerda
                                        }
                                    }

                                    if (isVertical) {
                                        if (dy < 0) {
                                            respostas[currentPage] = -1 // cima
                                        } else {
                                            respostas[currentPage] = 1 // baixo
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
                            val finalRespostas = respostas.map { it ?: 0 }
                            println("Respostas: $finalRespostas")
                            viewModel.calcularRankingCondorcet(finalRespostas)
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
