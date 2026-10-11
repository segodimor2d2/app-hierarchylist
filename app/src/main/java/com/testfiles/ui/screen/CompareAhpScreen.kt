package com.testfiles.ui.screen

import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.testfiles.viewmodel.SharedViewModel

@Composable
fun CompareAhpScreen(navController: NavController, viewModel: SharedViewModel) {
    val pairs by viewModel.itemPairs.collectAsState()
    var answers by rememberSaveable(pairs) { mutableStateOf(List<Double?>(pairs.size) { null }) }
    var positionsX by rememberSaveable(pairs) { mutableStateOf(List<Float?>(pairs.size) { null }) }
    var positionsY by rememberSaveable(pairs) { mutableStateOf(List<Float?>(pairs.size) { null }) }
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
                    val savedPosition = positionsX[page]?.let { x ->
                        positionsY[page]?.let { y -> Offset(x, y) }
                    }
                    AhpTouchArea(pair.first, pair.second, savedPosition) { position ->
                        val ratio = ahpSelectionAt(position.x, position.y, 1f, 1f).ratio
                        answers = answers.toMutableList().also { it[page] = ratio }
                        positionsX = positionsX.toMutableList().also { it[page] = position.x }
                        positionsY = positionsY.toMutableList().also { it[page] = position.y }
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
        x < width * 0.4f -> -1
        x < width * 0.6f -> 0
        else -> 1
    }
    val band = (y / height * 5).toInt().coerceIn(0, 4)
    return AhpTouchSelection(side, if (side == 0) 1 else 9 - band * 2)
}

@Composable
internal fun AhpTouchArea(
    first: String,
    second: String,
    savedPosition: Offset?,
    onAnswer: (Offset) -> Unit
) {
    var previewPosition by remember { mutableStateOf<Offset?>(null) }
    val callback by rememberUpdatedState(onAnswer)
    val position = previewPosition ?: savedPosition
    val selection = position?.let { ahpSelectionAt(it.x, it.y, 1f, 1f) }
    val levels = listOf(9, 7, 5, 3, 1)
    // Color follows height even when the middle zone records equality.
    val heightIntensity = position?.let { ahpSelectionAt(0f, it.y, 1f, 1f).intensity }
    val selectionColor = when (heightIntensity) {
        1 -> Color(0xFF43A047)
        3 -> Color(0xFF9EAD32)
        5 -> Color(0xFFFBC02D)
        7 -> Color(0xFFFB8C00)
        9 -> Color(0xFFE53935)
        else -> MaterialTheme.colorScheme.onSurface
    }
    val markerColor = selectionColor.copy(alpha = 0.5f)
    val markerOutline = selectionColor.copy(alpha = 0.65f)
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = when (selection?.side) {
                -1 -> first
                0 -> "Igualmente importantes"
                1 -> second
                else -> "Toque ou deslize para escolher"
            },
            modifier = Modifier.fillMaxWidth(),
            color = selectionColor,
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
            color = selectionColor,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                .semantics {
                    customActions = listOf(CustomAccessibilityAction("Igualmente importantes") {
                        callback(Offset(0.5f, 0.5f)); true
                    }) + listOf(-1 to first, 1 to second).flatMap { (side, label) ->
                        levels.map { level ->
                            CustomAccessibilityAction("$label, intensidade $level") {
                                callback(Offset(if (side == -1) 0.2f else 0.8f, (10 - level) / 10f))
                                true
                            }
                        }
                    }
                }
                .pointerInput(first, second) {
                    fun normalizedPosition(touch: Offset) = Offset(
                        (touch.x / size.width).coerceIn(0f, 1f),
                        (touch.y / size.height).coerceIn(0f, 1f)
                    )
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        down.consume()
                        previewPosition = normalizedPosition(down.position)
                        try {
                            while (true) {
                                val touch = awaitPointerEvent().changes.firstOrNull { it.id == down.id } ?: break
                                previewPosition = normalizedPosition(touch.position)
                                touch.consume()
                                if (!touch.pressed) {
                                    if (touch.previousPressed) callback(requireNotNull(previewPosition))
                                    break
                                }
                            }
                        } finally {
                            previewPosition = null
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
                        (it.x * size.width).coerceIn(radius, size.width - radius),
                        (it.y * size.height).coerceIn(radius, size.height - radius)
                    )
                    drawCircle(markerColor, radius, center)
                    drawCircle(markerOutline, radius, center, style = Stroke(2.dp.toPx()))
                }
            }
        }
    }
}
