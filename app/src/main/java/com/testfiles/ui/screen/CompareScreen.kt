package com.testfiles.ui.screen

import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.testfiles.viewmodel.SharedViewModel
import kotlinx.coroutines.launch
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompareScreen(
    navController: NavController,
    viewModel: SharedViewModel
) {
    val itemPairs by viewModel.itemPairs.collectAsState()
    var showSweepSpace by remember { mutableStateOf(true) }

    val respostas = remember {
        mutableStateListOf<Pair<Boolean, Boolean>?>().apply {
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
                val pagerState = rememberPagerState(initialPage = 0, pageCount = { total })
                val currentPage = pagerState.currentPage

                LaunchedEffect(currentPage) {
                    if (respostas[currentPage] == null) {
                        respostas[currentPage] = Pair(false, false)
                    }
                }

                val allAnswered = respostas.none { it == null }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 90.dp) // espaço para o botão fixo
                        .align(Alignment.TopStart)
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
                            Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                        }

                        Text(
                            text = "Proceso de Hierarquia",
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }

                    Spacer(modifier = Modifier.height(26.dp))

                    Text(
                        text = "Qual item é o mais importante?",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Se os dois forem igualmente importantes, selecione ambos.",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    Box(modifier = Modifier.fillMaxWidth()) {
                        HorizontalPager(state = pagerState) { page ->
                            val currentPair = itemPairs[page]
                            val currentSelection = respostas[page] ?: Pair(false, false)
                            val (firstSelected, secondSelected) = currentSelection

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp),
                                verticalArrangement = Arrangement.spacedBy(22.dp)
                            ) {
                                Text(
                                    text = "${page + 1} de $total",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(end = 16.dp)
                                )

                                Button(
                                    onClick = {
                                        respostas[page] = currentSelection.copy(first = !firstSelected)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally),
                                    shape = RoundedCornerShape(50.dp),
                                    border = if (firstSelected) null else BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (firstSelected)
                                            MaterialTheme.colorScheme.primary else Color.Transparent,
                                        contentColor = if (firstSelected)
                                            MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text(
                                        text = currentPair.first,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }

                                Button(
                                    onClick = {
                                        respostas[page] = currentSelection.copy(second = !secondSelected)
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally),
                                    shape = RoundedCornerShape(50.dp),
                                    border = if (secondSelected) null else BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (secondSelected)
                                            MaterialTheme.colorScheme.primary else Color.Transparent,
                                        contentColor = if (secondSelected)
                                            MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
                                    )
                                ) {
                                    Text(
                                        text = currentPair.second,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                    }


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .pointerInput(pagerState.currentPage) {
                                detectDragGestures { change, dragAmount ->
                                    change.consumeAllChanges()
                                    val (dx, dy) = dragAmount
                                    if (abs(dx) > abs(dy)) {
                                        val targetPage = when {
                                            dx < -10 -> pagerState.currentPage + 1
                                            dx > 10 -> pagerState.currentPage - 1
                                            else -> pagerState.currentPage
                                        }.coerceIn(0, total - 1)

                                        if (targetPage != pagerState.currentPage) {
                                            scope.launch {
                                                pagerState.animateScrollToPage(targetPage)
                                            }
                                        }
                                    }
                                }
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                        ) {
                            repeat(total) { index ->
                                val isSelected = index == pagerState.currentPage
                                val color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.3f)
                                Box(
                                    modifier = Modifier
                                        .padding(horizontal = 4.dp, vertical = 22.dp)
                                        .size(if (isSelected) 10.dp else 8.dp)
                                        .clip(RoundedCornerShape(percent = 50))
                                        .background(color)
                                )
                            }
                        }
                    }

                }


                // SweepSpace fixo acima do botão
                if (showSweepSpace) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(bottom = 90.dp) // espaço entre sweepSpace e botão
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.background)
                            .border(1.dp, Color.Gray.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                            .pointerInput(pagerState.currentPage) {
                                detectDragGestures { change, dragAmount ->
                                    change.consumeAllChanges()
                                    val (dx, dy) = dragAmount
                                    val absDx = abs(dx)
                                    val absDy = abs(dy)

                                    val isHorizontal = absDx > absDy
                                    val isVertical = absDy > absDx
                                    val nextPage = pagerState.currentPage + 1

                                    if (isHorizontal) {
                                        respostas[pagerState.currentPage] = Pair(true, true)
                                    }

                                    if (isVertical) {
                                        respostas[pagerState.currentPage] = if (dy < 0) Pair(true, false) else Pair(false, true)
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
                            color = Color.Gray.copy(alpha = 0.05f),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }


                // Botão fixo no final da tela
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Button(
                        onClick = {
                            val finalRespostas = respostas.map {
                                when (it) {
                                    Pair(true, false) -> -1
                                    Pair(false, true) -> 1
                                    else -> 0
                                }
                            }
                            println("Respostas: $finalRespostas")
                            viewModel.calcularRankingCondorcet(finalRespostas)
                            navController.navigate("ranking")
                        },
                        enabled = allAnswered,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Processar",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
