package com.testfiles.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.testfiles.viewmodel.SharedViewModel
import kotlin.math.roundToInt

@Composable
fun CompareAhpScreen(navController: NavController, viewModel: SharedViewModel) {
    val items by viewModel.itemList.collectAsState()
    val pairs by viewModel.itemPairs.collectAsState()
    // Save explicit judgments; visiting a pair does not count as answering it.
    var answers by rememberSaveable(pairs) { mutableStateOf(List<Double?>(pairs.size) { null }) }
    var page by rememberSaveable(pairs) { mutableIntStateOf(0) }
    var preference by rememberSaveable(page, pairs) { mutableIntStateOf(0) }
    var intensity by rememberSaveable(page, pairs) { mutableFloatStateOf(1f) }

    LaunchedEffect(page, answers.getOrNull(page)) {
        val ratio = answers.getOrNull(page) ?: 1.0
        preference = when { ratio > 1 -> 1; ratio < 1 -> -1; else -> 0 }
        intensity = (if (ratio < 1) 1 / ratio else ratio).toFloat()
    }

    fun recordAnswer() {
        val ratio = when (preference) {
            1 -> intensity.roundToInt().toDouble()
            -1 -> 1.0 / intensity.roundToInt()
            else -> 1.0
        }
        answers = answers.toMutableList().also { it[page] = ratio }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize().systemBarsPadding().padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TextButton(onClick = { navController.popBackStack() }) { Text("Voltar") }
            Text("Processo de Hierarquia AHP", style = MaterialTheme.typography.titleLarge)
            Text("Compare a importância dos itens em relação ao mesmo objetivo.")
            if (items.size < 2 || pairs.isEmpty()) {
                Text("Adicione pelo menos dois itens à lista para comparar.")
            } else {
                val pair = pairs[page]
                Text("${page + 1} de ${pairs.size} • ${answers.count { it != null }} respondidas")
                Text("Qual item é mais importante?", style = MaterialTheme.typography.titleMedium)
                listOf(1 to pair.first, 0 to "Igualmente importantes", -1 to pair.second).forEach { (value, label) ->
                    OutlinedButton(
                        onClick = {
                            preference = value
                            if (value != 0 && intensity < 2f) intensity = 2f
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (preference == value) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surface
                        )
                    ) { Text(label) }
                }
                if (preference != 0) {
                    Text("Intensidade: ${intensity.roundToInt()}")
                    Slider(
                        value = intensity.coerceIn(2f, 9f),
                        onValueChange = { intensity = it.roundToInt().toFloat() },
                        valueRange = 2f..9f,
                        steps = 6
                    )
                }
                Text("Escala: 1 = igual; 3 = moderada; 5 = forte; 7 = muito forte; 9 = extrema. 2, 4, 6 e 8 são intermediários.")
                Button(onClick = {
                    recordAnswer()
                    if (page < pairs.lastIndex) page++
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (page < pairs.lastIndex) "Confirmar e avançar" else "Confirmar comparação")
                }
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
