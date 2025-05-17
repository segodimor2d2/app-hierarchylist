package com.testfiles.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    val pagerState = rememberPagerState(initialPage = 0, pageCount = { total })
                    val currentPage = pagerState.currentPage

                    // Define resposta como 0 (empate) ao entrar na página se ainda for null
                    LaunchedEffect(currentPage) {
                        if (respostas[currentPage] == null) {
                            respostas[currentPage] = 0
                        }
                    }

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
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "${currentPage + 1} de $total",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier
                                        .align(Alignment.End)
                                        .padding(0.dp, 0.dp, 16.dp, 0.dp)
                                )
                                Button(
                                    onClick = {
                                        respostas[page] = if (respostas[page] == -1) 0 else -1
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.CenterHorizontally),
                                    shape = RoundedCornerShape(50.dp),
                                    border = if (respostas[page] == -1) null else BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (respostas[page] == -1)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            Color.Transparent, // fundo transparente para outlined
                                        contentColor = if (respostas[page] == -1)
                                            MaterialTheme.colorScheme.onPrimary
                                        else
                                            MaterialTheme.colorScheme.primary // cor do texto igual a cor da borda
                                    )
                                ){
                                    Text(
                                        text = currentPair.first,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }

                                Button(
                                    onClick = {
                                        respostas[page] = if (respostas[page] == 1) 0 else 1
                                    },
                                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally),
                                    shape = RoundedCornerShape(50.dp),
                                    border = if (respostas[page] == 1) null else BorderStroke(2.dp, MaterialTheme.colorScheme.primary),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (respostas[page] == 1)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            Color.Transparent, // fundo transparente para outlined
                                        contentColor = if (respostas[page] == 1)
                                            MaterialTheme.colorScheme.onPrimary
                                        else
                                            MaterialTheme.colorScheme.primary // cor do texto igual a cor da borda
                                    ),
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
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(total) { index ->
                            val isSelected = index == pagerState.currentPage
                            val color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.3f)
                            Box(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(if (isSelected) 10.dp else 8.dp)
                                    .clip(RoundedCornerShape(percent = 50))
                                    .background(color)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))


                    if (showSweepSpace) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.background)
                                .border(
                                    width = 1.dp,
                                    color = Color.Gray.copy(alpha = 0.05f), // borda sutil, quase do tom do fundo
                                    shape = RoundedCornerShape(8.dp)
                                )
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
                                color = Color.Gray.copy(alpha = 0.05f),
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
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors()
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
