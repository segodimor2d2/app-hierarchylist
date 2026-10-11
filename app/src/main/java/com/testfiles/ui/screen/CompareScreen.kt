package com.testfiles.ui.screen

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.Stroke
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
    var positionsX by rememberSaveable(itemPairs) {
        mutableStateOf(List<Float?>(itemPairs.size) { null })
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
                                    positionsX = positionsX.toMutableList().also { it[page] = if (value == -1) 0.2f else 0.8f }
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
                            savedPosition = positionsX[page],
                            enabled = !pagerState.isScrollInProgress,
                            onAnswer = { position ->
                                positionsX = positionsX.toMutableList().also { it[page] = position }
                                respostas = respostas.toMutableList().also { it[page] = mbcAnswerAt(position, 1f) }
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
    savedPosition: Float?,
    enabled: Boolean = true,
    onAnswer: (Float) -> Unit
) {
    var preview by remember(first, second) { mutableStateOf<Float?>(null) }
    val answerCallback by rememberUpdatedState(onAnswer)
    val labels = listOf(first, "Igualmente importantes", second)
    val positions = listOf(0.2f, 0.5f, 0.8f)
    val position = preview ?: savedPosition
    val selection = position?.let { mbcAnswerAt(it, 1f) }
    val selectionColor = MaterialTheme.colorScheme.primary
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier.fillMaxWidth().height(160.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                .semantics {
                    customActions = if (enabled) labels.mapIndexed { index, label ->
                        CustomAccessibilityAction(label) { answerCallback(positions[index]); true }
                    } else emptyList()
                }
                .pointerInput(first, second, enabled) {
                    if (!enabled) return@pointerInput
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        down.consume()
                        preview = (down.position.x / size.width).coerceIn(0f, 1f)
                        try {
                            while (true) {
                                val event = awaitPointerEvent()
                                val touch = event.changes.firstOrNull { it.id == down.id } ?: break
                                preview = (touch.position.x / size.width).coerceIn(0f, 1f)
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
            Row(
                modifier = Modifier.fillMaxWidth().align(Alignment.Center),
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf(first to 0.4f, "=" to 0.2f, second to 0.4f).forEach { (label, weight) ->
                    Text(
                        label,
                        modifier = Modifier.weight(weight).padding(8.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            Canvas(modifier = Modifier.matchParentSize()) {
                position?.let {
                    val radius = 9.dp.toPx().coerceAtMost(size.minDimension / 2)
                    val center = Offset(
                        (it * size.width).coerceIn(radius, size.width - radius),
                        size.height / 2
                    )
                    drawCircle(selectionColor.copy(alpha = 0.5f), radius, center)
                    drawCircle(selectionColor.copy(alpha = 0.65f), radius, center, style = Stroke(2.dp.toPx()))
                }
            }
        }
        Text(
            text = when (selection) {
                -1 -> first
                0 -> "Igualmente importantes"
                1 -> second
                else -> "Toque ou deslize para escolher"
            },
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = selectionColor
        )
    }
}

internal fun mbcAnswerAt(x: Float, width: Float): Int {
    require(width > 0)
    return when {
        x < width * 0.4f -> -1
        x < width * 0.6f -> 0
        else -> 1
    }
}
