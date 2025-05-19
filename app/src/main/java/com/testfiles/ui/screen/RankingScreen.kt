package com.testfiles.ui.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.testfiles.viewmodel.SharedViewModel
import org.burnoutcrew.reorderable.*

@Composable
fun RankingScreen(navController: NavController, viewModel: SharedViewModel) {
    val context = LocalContext.current
    val fileUri by viewModel.selectedFileUri.collectAsState()
    val ranking by viewModel.ranking.collectAsState()
    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Estado da lista local para manipulação do ranking na UI
    var rankingState by remember { mutableStateOf(ranking) }

    // Atualiza o rankingState caso o ranking da ViewModel seja alterado (ex.: carregamento inicial)
    LaunchedEffect(ranking) {
        rankingState = ranking
    }

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
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .padding(bottom = 136.dp)
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
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }

                    Text(
                        text = "Ranking",
                        style = MaterialTheme.typography.titleLarge,
                    )
                }

                Spacer(modifier = Modifier.height(26.dp))

                Text(
                    text = "Ranking",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Itens classificados por ordem de importância",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(26.dp))

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                }


                DraggableRankingList(rankingState) { from, to ->
                    val mutableList = rankingState.toMutableList()
                    val item = mutableList.removeAt(from)
                    mutableList.add(to, item)
                    rankingState = mutableList
                }

                message?.let { msg ->
                    Text(
                        text = msg,
                        color = if (msg.startsWith("Erro")) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Box(
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.saveRanking(context, rankingState)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text(
                            text = "Salvar lista",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Button(
                        onClick = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = false }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                    ) {
                        Text(
                            text = "Finalizar",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DraggableRankingList(
    ranking: List<Pair<String, Int>>,
    onMove: (Int, Int) -> Unit
) {
    val state = rememberReorderableLazyListState(onMove = { from, to ->
        onMove(from.index, to.index)
    })

    LazyColumn(
        state = state.listState,
        modifier = Modifier
            .reorderable(state)
            .detectReorderAfterLongPress(state)
    ) {
        itemsIndexed(ranking, key = { _, item -> item.first }) { index, (item, score) ->
            ReorderableItem(state, key = item) { isDragging ->
                val backgroundColor = if (isDragging)
                    MaterialTheme.colorScheme.surfaceVariant
                else
                    MaterialTheme.colorScheme.surface

                val borderStroke = if (isDragging)
                    BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                else
                    BorderStroke(1.dp, MaterialTheme.colorScheme.outline)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    border = borderStroke,
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = backgroundColor)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 18.dp)
                            .animateItemPlacement(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${index + 1}. $item (${score} pts)",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Arraste",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
