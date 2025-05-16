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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    val itemPairs by viewModel.itemPairs.collectAsState()
    var showSweepSpace by remember { mutableStateOf(false) }
    val respostas = remember {
        mutableStateListOf<Int?>().apply {
            repeat(itemPairs.size) { add(null) }
        }
    }
    val scope = rememberCoroutineScope()
    val total = itemPairs.size

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
            if (total > 0) {
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

                    Text(
                        text = "Qual item é mais importante?",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

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

                    Spacer(modifier = Modifier.height(8.dp))


                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth()
                            //.height(500.dp)
                    ) {

                        HorizontalPager(
                            state = pagerState,
                            modifier = Modifier
                                //.height(300.dp)
                        ) { page ->
                            val currentPair = itemPairs[page]


                            Column(
                                modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 4.dp),
                                //.padding(bottom = 136.dp), // 104dp botão + 48dp de padding
                                verticalArrangement = Arrangement.spacedBy(22.dp)
                            ) {
                                Text(
                                    text = "A: ${currentPair.first}\nB: ${currentPair.second}",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                Text(
                                    text = "${currentPage + 1} de $total",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.align(Alignment.End)
                                )
                                Button(
                                    onClick = {
                                        respostas[page] = if (respostas[page] == -1) 0 else -1
                                    },
                                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                                    shape = RoundedCornerShape(4.dp),
                                    border = if (respostas[page] == -1) BorderStroke(2.dp, Color.White) else null,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (respostas[page] == -1)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.secondary
                                            //optionColors[-1]!!
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = currentPair.first,
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.padding(4.dp)
                                        )
                                        Text(
                                            text = "é mais importante do que",
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(4.dp)
                                        )
                                        Text(
                                            text = "${currentPair.second}?",
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.padding(4.dp)
                                        )
                                    }
                                }

                                Button(
                                    onClick = {
                                        respostas[page] = if (respostas[page] == 1) 0 else 1
                                    },
                                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                                    shape = RoundedCornerShape(4.dp),
                                    border = if (respostas[page] == 1) BorderStroke(2.dp, Color.White) else null,
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (respostas[page] == 1)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.secondary
                                            //optionColors[1]!!
                                            //Color(0xFFE0E0E0)
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {                                        Text(
                                        text = currentPair.second,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                        Text(
                                            text = "é mais importante do que",
                                            style = MaterialTheme.typography.bodySmall,
                                            modifier = Modifier.padding(4.dp)
                                        )
                                        Text(
                                            text = "${currentPair.first}?",
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier.padding(4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 80.dp) // Garante uma altura mínima para evitar "pulos"
                            .clickable { navController.popBackStack() }
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = when (selected) {
                                -1 -> "${currentPair?.first}\né mais importante do que\n${currentPair?.second}?"
                                1 -> "${currentPair?.second}\né mais importante do que\n${currentPair?.first}?"
                                0 -> "Os dois são igual de importantes?"
                                else -> "Nenhuma escolha feita."
                            },
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (showSweepSpace) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF2B2D30))
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
                            Text(
                                text = "Deslize aqui para escolher",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                    }

                }


                Spacer(modifier = Modifier.height(16.dp))

                val allAnswered = respostas.none { it == null }

                Box(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .align(Alignment.BottomCenter)
                ) {
                    Button(
                        onClick = {
                            val finalRespostas = respostas.map { it ?: 0 }
                            println("Respostas: $finalRespostas")
                            viewModel.calcularRankingCondorcet(finalRespostas)
                            navController.navigate("ranking")
                        },
                        enabled = allAnswered,
                        modifier = Modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(4.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (allAnswered) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    ) {
                        Text(
                            text = "Processar",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }


            } else {
                Text(
                    text = "Nenhum par disponível para comparar.",
                    //modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

        }
    }
}
