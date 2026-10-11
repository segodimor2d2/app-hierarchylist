package com.testfiles.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.*
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
import kotlin.math.roundToInt

@Composable
fun CompareAhpScreen(navController: NavController, viewModel: SharedViewModel) {
    val pairs by viewModel.itemPairs.collectAsState()
    var answers by rememberSaveable(pairs) { mutableStateOf(List<Double?>(pairs.size) { null }) }
    var page by rememberSaveable(pairs) { mutableIntStateOf(0) }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().systemBarsPadding().padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = { navController.popBackStack() }) { Text("Voltar") }
            Text("Processo de Hierarquia AHP", style = MaterialTheme.typography.titleLarge)
            Text("Compare a importância dos itens em relação ao mesmo objetivo.")
            if (pairs.isEmpty()) {
                Text("Adicione pelo menos dois itens à lista para comparar.")
            } else {
                val pair = pairs[page]
                Text("${page + 1} de ${pairs.size} • ${answers.count { it != null }} respondidas")
                Text("Qual item é mais importante?", style = MaterialTheme.typography.titleMedium)
                Text("Deslize para escolher o item e a intensidade. Ao soltar, a resposta será salva e a próxima pergunta será exibida.")
                key(page, pairs) {
                    AhpTouchArea(pair.first, pair.second, answers[page]) { ratio ->
                        answers = answers.toMutableList().also { it[page] = ratio }
                        if (page < pairs.lastIndex) page++
                    }
                }
                Text("Escala: 1 = igual; 3 = moderada; 5 = forte; 7 = muito forte; 9 = extrema.")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = { page-- }, enabled = page > 0) { Text("Anterior") }
                    TextButton(onClick = { page++ }, enabled = page < pairs.lastIndex) { Text("Próxima") }
                }
                Button(
                    onClick = {
                        viewModel.calcularRankingAhp(answers.map { requireNotNull(it) })
                        navController.navigate("ranking")
                    },
                    enabled = answers.all { it != null },
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Processar AHP") }
            }
        }
    }
}

internal data class AhpTouchSelection(val side: Int, val intensity: Int) {
    val ratio: Double get() = when (side) {
        -1 -> intensity.toDouble()
        1 -> 1.0 / intensity
        else -> 1.0
    }
}

internal fun ahpSelectionAt(x: Float, y: Float, width: Float, height: Float): AhpTouchSelection {
    require(width > 0 && height > 0)
    val side = when {
        x < width / 3 -> -1
        x < width * 2 / 3 -> 0
        else -> 1
    }
    val band = (y / height * 5).toInt().coerceIn(0, 4)
    return AhpTouchSelection(side, if (side == 0) 1 else 9 - band * 2)
}

@Composable
internal fun AhpTouchArea(first: String, second: String, savedAnswer: Double?, onAnswer: (Double) -> Unit) {
    var preview by remember { mutableStateOf<AhpTouchSelection?>(null) }
    val callback by rememberUpdatedState(onAnswer)
    val savedSelection = savedAnswer?.let {
        AhpTouchSelection(
            when { it > 1 -> -1; it < 1 -> 1; else -> 0 },
            (if (it < 1) 1 / it else it).roundToInt()
        )
    }
    val selection = preview ?: savedSelection
    val labels = listOf(first, "Igualmente importantes", second)
    val levels = listOf(9, 7, 5, 3, 1)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(modifier = Modifier.fillMaxWidth()) {
            labels.forEach { label ->
                Text(label, modifier = Modifier.weight(1f).padding(4.dp), textAlign = TextAlign.Center)
            }
        }
        Column(
            modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                .semantics {
                    customActions = listOf(CustomAccessibilityAction("Igualmente importantes") { callback(1.0); true }) +
                        listOf(-1 to first, 1 to second).flatMap { (side, label) ->
                            levels.map { level ->
                                CustomAccessibilityAction("$label, intensidade $level") {
                                    callback(AhpTouchSelection(side, level).ratio)
                                    true
                                }
                            }
                        }
                }
                .pointerInput(first, second) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        down.consume()
                        preview = ahpSelectionAt(down.position.x, down.position.y, size.width.toFloat(), size.height.toFloat())
                        try {
                            while (true) {
                                val touch = awaitPointerEvent().changes.firstOrNull { it.id == down.id } ?: break
                                preview = ahpSelectionAt(touch.position.x, touch.position.y, size.width.toFloat(), size.height.toFloat())
                                touch.consume()
                                if (!touch.pressed) {
                                    if (touch.previousPressed) callback(requireNotNull(preview).ratio)
                                    break
                                }
                            }
                        } finally {
                            preview = null
                        }
                    }
                }
        ) {
            levels.forEach { level ->
                Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    listOf(-1, 0, 1).forEach { side ->
                        val highlighted = selection?.side == side && (side == 0 || selection.intensity == level)
                        Box(
                            modifier = Modifier.weight(1f).fillMaxHeight()
                                .background(if (highlighted) MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.surfaceVariant)
                                .border(0.5.dp, MaterialTheme.colorScheme.outline),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(if (side == 0) "=" else level.toString())
                        }
                    }
                }
            }
        }
        Text(
            text = when (selection?.side) {
                -1 -> first
                0 -> "Igualmente importantes"
                1 -> second
                else -> "Toque ou deslize para escolher"
            },
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = selection?.let {
                val description = when (it.intensity) {
                    1 -> "igual"
                    3 -> "moderada"
                    5 -> "forte"
                    7 -> "muito forte"
                    else -> "extrema"
                }
                "Intensidade: ${it.intensity} — $description"
            } ?: "Intensidade: deslize de baixo (1) para cima (9)",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}
