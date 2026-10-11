package com.testfiles.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.testfiles.viewmodel.SharedViewModel
import kotlinx.coroutines.launch

@Composable
fun CompareScreen(navController: NavController, viewModel: SharedViewModel) {
    val itemPairs by viewModel.itemPairs.collectAsState()
    var respostas by rememberSaveable(itemPairs) {
        mutableStateOf(List<Int?>(itemPairs.size) { null })
    }
    val scope = rememberCoroutineScope()

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().systemBarsPadding().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = { navController.popBackStack() }) { Text("Voltar") }
            Text("Processo de Hierarquia MBC", style = MaterialTheme.typography.titleLarge)
            if (itemPairs.isEmpty()) {
                Text("Adicione pelo menos dois itens à lista para comparar.")
            } else {
                val pagerState = rememberPagerState(pageCount = { itemPairs.size })
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) { page ->
                    val pair = itemPairs[page]
                    Column(
                        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text("${page + 1} de ${itemPairs.size}", style = MaterialTheme.typography.titleMedium)
                        Text("Qual item é o mais importante?", style = MaterialTheme.typography.headlineSmall)
                        listOf(-1 to "A: ${pair.first}", 1 to "B: ${pair.second}").forEach { (value, label) ->
                            OutlinedButton(
                                onClick = {
                                    respostas = respostas.toMutableList().also { it[page] = value }
                                },
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = if (respostas[page] == value || respostas[page] == 0)
                                        MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                                )
                            ) { Text(label) }
                        }
                        Text("Deslize na área abaixo. Ao soltar, sua resposta será salva e a próxima pergunta será exibida.")
                        MbcTouchArea(
                            first = pair.first,
                            second = pair.second,
                            savedAnswer = respostas[page],
                            enabled = !pagerState.isScrollInProgress,
                            onAnswer = { answer ->
                                respostas = respostas.toMutableList().also { it[page] = answer }
                                if (page < itemPairs.lastIndex) {
                                    scope.launch { pagerState.animateScrollToPage(page + 1) }
                                }
                            }
                        )
                    }
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    TextButton(
                        onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage - 1) } },
                        enabled = pagerState.currentPage > 0 && !pagerState.isScrollInProgress
                    ) { Text("Anterior") }
                    TextButton(
                        onClick = { scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } },
                        enabled = pagerState.currentPage < itemPairs.lastIndex && !pagerState.isScrollInProgress
                    ) { Text("Próxima") }
                }
                Button(
                    onClick = {
                        viewModel.calcularRankingCondorcet(respostas)
                        navController.navigate("ranking")
                    },
                    enabled = respostas.all { it != null },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Processar", modifier = Modifier.padding(vertical = 8.dp)) }
            }
        }
    }
}

@Composable
internal fun MbcTouchArea(
    first: String,
    second: String,
    savedAnswer: Int?,
    enabled: Boolean = true,
    onAnswer: (Int) -> Unit
) {
    var preview by remember(first, second) { mutableStateOf<Int?>(null) }
    val answerCallback by rememberUpdatedState(onAnswer)
    val labels = listOf(first, "Igualmente importantes", second)
    val values = listOf(-1, 0, 1)
    val highlighted = preview ?: savedAnswer
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().height(160.dp)
                .clip(RoundedCornerShape(8.dp))
                .border(BorderStroke(1.dp, MaterialTheme.colorScheme.outline), RoundedCornerShape(8.dp))
                .semantics {
                    customActions = if (enabled) labels.mapIndexed { index, label ->
                        CustomAccessibilityAction(label) { answerCallback(values[index]); true }
                    } else emptyList()
                }
                .pointerInput(first, second, enabled) {
                    if (!enabled) return@pointerInput
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        down.consume()
                        preview = mbcAnswerAt(down.position.x, size.width.toFloat())
                        try {
                            while (true) {
                                val event = awaitPointerEvent()
                                val touch = event.changes.firstOrNull { it.id == down.id } ?: break
                                preview = mbcAnswerAt(touch.position.x, size.width.toFloat())
                                touch.consume()
                                if (!touch.pressed) {
                                    if (touch.previousPressed) answerCallback(requireNotNull(preview))
                                    break
                                }
                            }
                        } finally {
                            preview = null
                        }
                    }
                }
        ) {
            labels.forEachIndexed { index, label ->
                val isHighlighted = highlighted == values[index]
                Box(
                    modifier = Modifier.weight(1f).fillMaxHeight()
                        .background(if (isHighlighted) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant)
                        .border(0.5.dp, MaterialTheme.colorScheme.outline)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) { Text(label, textAlign = TextAlign.Center) }
            }
        }
        Text(
            text = when (highlighted) {
                -1 -> first
                0 -> "Igualmente importantes"
                1 -> second
                else -> "Toque ou deslize para escolher"
            },
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

internal fun mbcAnswerAt(x: Float, width: Float): Int {
    require(width > 0)
    return when {
        x < width / 3f -> -1
        x < width * 2f / 3f -> 0
        else -> 1
    }
}
